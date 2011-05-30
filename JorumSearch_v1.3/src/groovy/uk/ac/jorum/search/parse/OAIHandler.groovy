package uk.ac.jorum.search.parse

import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.*
import org.apache.log4j.*
import uk.ac.jorum.search.domain.*;
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import uk.ac.jorum.search.parse.ISO8601DateParser;


public class OAIHandler extends DefaultHandler {
	
	final String DC_NAMESPACE = "http://purl.org/dc/elements/1.1/"
	final String OAI_DC_NAMESPACE = "http://www.openarchives.org/OAI/2.0/oai_dc/"
	final String OAI_NAMESPACE = "http://www.openarchives.org/OAI/2.0/"
	
	def batch = []
	static batchLimit = 50
	
	
	def buffer
	def inMeta = false
	def currElem
	def m = [:]
	def item = null
	def count = 0
	def log = Logger.getInstance(OAIHandler.class)
	
	def itemStartTime = 0
	def ctx = AH.application.mainContext
	def sessionFactory=ctx.sessionFactory
	def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
	def target
	
	def itemIdToDelete
	
	
	void startElement(String ns, String localName, String qName, Attributes atts) {
		log.debug "Processing Element ${ns} ${localName} ${qName}"
		
		currElem = localName
		
		
		
		
		switch (localName) {
			case 'record':
				if (ns.compareToIgnoreCase(OAI_NAMESPACE) == 0){
					log.debug "Found OAI record element, creating new item"
					itemStartTime = System.currentTimeMillis()
					
					item = new Item()
				}
				break
			case 'dc':
			// Ensure we are in the right namespace
			//if (ns.compareToIgnoreCase(OAI_DC_NAMESPACE) == 0){
			//	println "Found OAI-DC element, creating new item"
			//	item = new Item()
			//}
				break
			case 'header':
			// Check the header to see if the status attribute is set to 'deleted'			
				int statusIndex = atts.getIndex("status")
				if(statusIndex!=null && statusIndex>=0){
					if(atts.getValue(statusIndex).compareToIgnoreCase("deleted") == 0 ){
						// We need to ensure this item isn't in the db or index
						item = new Item()
						item.toBeDeleted=true
					}
				}
				break
			default:
				break
		}
	}
	
	
	void endElement(String ns, String localName, String qName) {
		
		// Process elements in the OAI namespace
		if (ns.compareToIgnoreCase(OAI_NAMESPACE) == 0){
			switch (localName) {
				case 'identifier':
				
				// Store the OAI identifier directly in the item - don't use the metadata hash as there could be a confilct in the key name
					item.oai_identifier = buffer.trim()
				
				
				
				// nonw see if we already have an item in the DB matching this identifier
					def storedItem = Item.findByOai_identifier(item.oai_identifier)
				
				
				
					if (storedItem){
						log.debug "Found existing item in DB with OAI identifier: ${item.oai_identifier}"
						
						
						if(item.toBeDeleted){
							
							log.info " Removing ${storedItem} from db and solr"
							
							try {
								storedItem.delete(flush:true)
								log.debug "Deleted ${storedItem}"
								// Note that this deletion will only come into effect until a solr commit
								// Won't commit here as this is expensive - a commit will be performed at the
								// end of re-indexing
								storedItem.deleteSolr()
							}
							catch(Exception e) {
								log.error "Could not delete person ${storedItem}: ${e}"
							}
							
						}else {
							// Make sure the date stamp is set ok (incase we have already parsed it)
							storedItem.oai_datestamp = item.oai_datestamp
							
							// Use this item instead rather than the new one!
							item = storedItem
						}
					}
				
				
					break
				
				case 'datestamp':
				// Store the OAI datestamp directly in the item - don't use the metadata hash as there could be a confilct in the key name
					String oaiDateFromXml = buffer.trim()
					Date oaiTimestamp = new Date()
					try{
						oaiTimestamp = ISO8601DateParser.parse(oaiDateFromXml)
					} catch (Exception e) {
						log.warn("Unable to parse oai timestamp oaiDateFromXml, using current time instead")
					}
				
					item.oai_datestamp = oaiTimestamp
					break
				
				case 'record':
				
				// Now set props in item
					log.debug "*************************"
					log.debug "Item hashmap: ${m}"
				
					item = target.parse(m, item)
				
				// If we reach the batch limit, persist to the database, clean GORM and clear the batch 
					if (batch.size() >= batchLimit) {
						writeBatch(batch)
					}
				// Don't add items that are to be deleted to the batch - will result in a error when saving
					if(!item.toBeDeleted){
						batch.add(item)
					}
				
				
				/*	log.debug "Saving item ...."
				 if(!item.save(deepValidate:false))  {
				 item.errors.allErrors.each { log.error it }
				 }
				 def total = (System.currentTimeMillis()-itemStartTime)/1000
				 log.info "${total} seconds to save ${item}"
				 itemStartTime=0
				 if (count % 50 == 0) cleanUpGorm()
				 log.debug "*************************\n"*/
				
					++count
				
				// reset the item pointer
					item = null
				
				// reset the metadata hash
					m.clear()
				
				
					break
				
				default:
				// do nothing
					break
			}
		}
		// Process elements in the DC namespace
		else if (ns.compareToIgnoreCase(DC_NAMESPACE) == 0){
			switch (localName) {
				default:
					if (item){
						// Check to see if we have seen this key before - if so append the value to the list
						if (m[currElem] != null){
							m[currElem].add(buffer.trim())
						} else {
							// Create a new list and add it
							m[currElem] = [ buffer.trim() ]
						}
						
						log.debug "Added key ${currElem} with value ${m[currElem]}"
					}
					break
			}
		}else if (localName.equalsIgnoreCase("harvest")){
			log.debug "Reached the end of the harvest xml - process items left in batch"
			if(batch.size()>0){
				writeBatch(batch)
			}
		}
		
		// Now reset the character buffer
		buffer = ""
	}
	
	// Writes a batch of items to the db and cleans up GORM afterwards
	def writeBatch = { batch ->
		def begin = System.currentTimeMillis()
		Item.withTransaction {
			for (Item i: batch){
				if(!i.save(deepValidate:false))  {
					i.errors.allErrors.each { log.error it}
				}
			}
			batch.clear()
			cleanUpGorm()
			log.debug "Cleared batch"
			def duration = (System.currentTimeMillis()-begin)/1000
			log.info "Took ${duration}s to add batch of ${batchLimit} items. Av of ${duration/batchLimit}s per item "
		}
	}
	
	/* To enable batch mode in Hibernate, we have to clean the session after a batch of writes, 
	 otherwise memory use will increase and performance will suffer.*/
	def cleanUpGorm() {
		def session = sessionFactory.currentSession
		// Order is important here - calling flush without first clearing the session causes duplicate creators, publishers
		// and subjects to be persisted to the database
		session.clear()
		session.flush()
		propertyInstanceMap.get().clear()
		log.debug "******************************"
		log.debug "******CLEANED UP GORM*********"
		log.debug "******************************"
	}
	
	
	void characters(char[] chars, int offset, int length) {
		if (item) {
			buffer = buffer + new String(chars, offset, length)
		}
	}
	
	int getCount(){
		return count
	}
}