package uk.ac.jorum.search.service

import javax.xml.parsers.SAXParserFactory
import org.xml.sax.*
import org.apache.log4j.*

import uk.ac.jorum.search.parse.*;
import uk.ac.jorum.search.domain.*;

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

import org.codehaus.groovy.grails.commons.metaclass.*
import org.apachedomainDesc.solr.client.solrj.impl.*
import org.apache.solr.common.*


import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsClassUtils

import org.grails.solr.Solr
import org.grails.solr.SolrUtil


class IndexMetadataService {
	
	static transactional = true
	def targets = [:]
	
	def handler=null
	
	def getSolrServer = {
		return AH.application.mainContext.getBean("solrService").getServer()
	}
	
	def refreshTargets = {
		targets.clear()
		// we need the eachFileMatch so we don't accidentally try to load .svn or anything else
		new File( CH.config.grails.target.parse ).eachFileMatch(~/.*\.groovy/){ file ->
			def config = new ConfigSlurper().parse( file.toURL() )
			targets.put( config.key, config )
			log.debug( "loaded targets ${config.name} from file ${ file }")
		}
	}
	

	/**
	 *  Takes a key and a filePath.  The key should match the key in one of the parser classes in
	 *  uk.ac.jorum.search.parse.target.  This parser will then be used to parse the file specified 
	 *  and the items generated will be stored in the database in batches.  
	 *  
	 *  Note: After calling this closure, it is advisable to call submitBatchesToSolr() which will
	 *  post the items stored in the database to Solr, followed by a commit. 
	 */
	def index = { targetKey, filePath ->
		
		//Just in case a new target has been added, we'll refresh
		refreshTargets.call()
		
		def f = new File(filePath)
		def inputStream = new FileInputStream(f)
		def handler = new OAIHandler()
		handler.target=targets[ targetKey ]
		def reader = SAXParserFactory.newInstance().newSAXParser().XMLReader
		
		println "XMLReader class is ${reader.class}"
		reader.setFeature("http://xml.org/sax/features/namespaces", true)
		reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true)
		reader.setContentHandler(handler)
		
		def indexStartTime = System.currentTimeMillis()
		reader.parse(new InputSource(inputStream))
		
		log.info "Harvest file has been parsed and loaded into DB."
		//		log.info "Harvest file has been parsed and loaded into DB.  Batching and sending to Solr..."
		//	submitBatchesToSolr()
		
		def totalTime = (System.currentTimeMillis() - indexStartTime)/1000
		
		log.info "Stored ${handler.getCount()} Items in ${totalTime} seconds (${totalTime/60} minutes) from ${filePath} in database "
	}
	
	
	// closure that batches collections of item documents together, Posts to Solr and finally commits
	def submitBatchesToSolr = {
		
		// Nasty hack alert!  This should be done via the Bootstrap, but I can't yet get it working when invoked via Runscript
		createSolrDocumentDomainMethod()
		
		def offset = 0
		def batchSize=50
		def total = Item.count()
		
		while(offset < total) {
			indexBatch(Item.list([max:batchSize, offset: offset]))
			log.info "Indexed ${offset} of ${total}"
			offset+=batchSize
		}
		commitToSolr()
	}
	
	def commitToSolr = {
		getSolrServer().commit()
		log.info "Committed all items to Solr"
	}
	
	// helper closure which creates a collection of solr documents from a list of items and adds to the solr server
	def indexBatch = { itemBatchList ->
		//Create a document for each item and add to collection
		Collection<SolrInputDocument> docs = itemBatchList.collect{it.getSolrDocument()
		}
		
		// This will post the batch of Solr Documents to the Solr server (but won't commit)
		getSolrServer().add(docs)
	}
	
	
	// Closure that adds a method to each of the domain classes which returns the appropriate solr document for each instance
	def createSolrDocumentDomainMethod = {
		def application = AH.application
		application.domainClasses.each { dc ->
			
			if(GrailsClassUtils.getStaticPropertyValue(dc.clazz, "enableSolrSearch")) {
				def domainDesc = application.getArtefact(DomainClassArtefactHandler.TYPE, dc.clazz.name)
				
				// define solrDocument() method for all domain classes
				dc.metaClass.getSolrDocument << { server = null ->
					def delegateDomainOjbect = delegate
					def doc = new SolrInputDocument();
					indexDomain(application, delegateDomainOjbect, doc)
					return doc
				}
			}
		}
	}
	
	
	//if called, this version of the indexSolr method won't commit the document sent to Solr (Not used at present)
	def overrideSolrIndexMethod = {
		def application = AH.application
		def ctx = AH.application.mainContext
		application.domainClasses.each { dc ->
			
			if(GrailsClassUtils.getStaticPropertyValue(dc.clazz, "enableSolrSearch")) {
				def domainDesc = application.getArtefact(DomainClassArtefactHandler.TYPE, dc.clazz.name)
				
				// define indexSolr() method for all domain classes
				dc.metaClass.indexSolr << { server = null ->
					def delegateDomainOjbect = delegate
					def solrService = ctx.getBean("solrService");
					if(!server)
					server = solrService.getServer()
					
					// create a new solr document
					def doc = new SolrInputDocument();
					
					indexDomain(application, delegateDomainOjbect, doc)
					
					server.add(doc)
					// server.commit()
					
				}
			} // if enable solr search
		} //domainClass.each
	}
	
	
	//This is a direct copy from the method in SolrGrailsPlugin.groovy - I wasn't able to access the original directly
	private indexDomain(application, delegateDomainOjbect, doc, depth = 1, prefix = "") {
		
		def domainDesc = application.getArtefact(DomainClassArtefactHandler.TYPE, delegateDomainOjbect.class.name)
		def clazz = (delegateDomainOjbect.class.name == 'java.lang.Class') ? delegateDomainOjbect : delegateDomainOjbect.class
		
		domainDesc.getProperties().each { prop ->
			
			//println "the type for ${it.name} is ${it.type}"
			// if the property is a closure, the type (by observation) is java.lang.Object
			// TODO: reconsider passing on all java.lang.Objects
			//println "${it.name} : ${it.type}"
			if(!SolrUtil.IGNORED_PROPS.contains(prop.name) && prop.type != java.lang.Object) {
				
				// look to see if the property has a solrIndex override method
				def overrideMethodName = (prop.name?.length() > 1) ? "indexSolr${prop.name[0].toUpperCase()}${prop.name.substring(1)}" : ""
				def overrideMethod = delegateDomainOjbect.metaClass.pickMethod(overrideMethodName)
				
				if(overrideMethod != null) {
					overrideMethod.invoke(delegateDomainOjbect, doc)
				}
				else if(delegateDomainOjbect."${prop.name}" != null) {
					def fieldName = delegateDomainOjbect.solrFieldName(prop.name);
					
					// fieldName may be null if the ignore annotion is used, not the best way to handle but ok for now
					if(fieldName) {
						def docKey = prefix + fieldName
						def docValue = delegateDomainOjbect.properties[prop.name]
						
						// Removed because of issues with stale indexing when composed index changes
						// Recursive indexing of composition fields
						//if(GrailsClassUtils.getStaticPropertyValue(docValue.class, "enableSolrSearch") && depth < 3) {
						//  def innerDomainDesc = application.getArtefact(DomainClassArtefactHandler.TYPE, docValue.class.name)
						//  indexDomain(application, docValue, doc, ++depth, "${docKey}.")
						//} else {
						//  doc.addField(docKey, docValue)
						//}
						
						// instead of the composition logic above, if the class is a domain class
						// then set the value to the Solr Id
						// TODO - reconsider this indexing logic as a whole
						if(DomainClassArtefactHandler.isDomainClass(docValue.class))
							doc.addField(docKey, SolrUtil.getSolrId(docValue))
						else
							doc.addField(docKey, docValue)
						
						// if the annotation asTextAlso is true, then also index this field as a text type independant of how else it's
						// indexed. The best way to handle the need to do this would be the properly configure the schema.xml file but
						// for those not familiar with Solr this is an easy way to make sure the field is processed as text which should
						// be the default search and processed with a WordDelimiterFilter
						
						def clazzProp = clazz.declaredFields.find{ field -> field.name == prop.name}
						if(clazzProp && clazzProp.isAnnotationPresent(Solr) && clazzProp.getAnnotation(Solr).asTextAlso()) {
							doc.addField("${prefix}${prop.name}_t", docValue)
						}
					}
					
					//println "Indexing: ${docKey} = ${docValue}"
				}
			} // if ignored props
		} // domainDesc.getProperties().each
		
		// add a field to the index for the field ype
		doc.addField(prefix + SolrUtil.TYPE_FIELD, delegateDomainOjbect.class.name)
		
		// add a field for the id which will be the classname dash id
		doc.addField("${prefix}id", "${delegateDomainOjbect.class.name}-${delegateDomainOjbect.id}")
		
		if(doc.getField(SolrUtil.TITLE_FIELD) == null) {
			def solrTitleMethod = delegateDomainOjbect.metaClass.pickMethod("solrTitle")
			def solrTitle = (solrTitleMethod != null) ? solrTitleMethod.invoke(delegateDomainOjbect) : delegateDomainOjbect.toString()
			doc.addField(SolrUtil.TITLE_FIELD, solrTitle)
		}
	} // indexDomain
	
}
