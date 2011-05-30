

import javax.xml.parsers.SAXParserFactory
import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.*
import org.apache.log4j.*

import uk.ac.jorum.search.domain.Item
import uk.ac.jorum.search.domain.Subject
import uk.ac.jorum.search.domain.Publisher
import uk.ac.jorum.search.domain.Creator
import org.hibernate.SessionFactory
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

import java.text.SimpleDateFormat
import uk.ac.jorum.search.parse.ISO8601DateParser;

class OAIHandler extends DefaultHandler {
	
    String configFileContents = ""
    
    final String DC_NAMESPACE = "http://purl.org/dc/elements/1.1/"
    final String OAI_DC_NAMESPACE = "http://www.openarchives.org/OAI/2.0/oai_dc/"
    final String OAI_NAMESPACE = "http://www.openarchives.org/OAI/2.0/"
    	
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
						
							// Make sure the date stamp is set ok (incase we have already parsed it)
							storedItem.oai_datestamp = item.oai_datestamp 
						
							// Use this item instead rather than the new one!
							item = storedItem
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

					Binding binding = new Binding();
					binding.setVariable("metaHash", m);
					binding.setVariable("item", item);
					binding.setVariable("log",log)
					GroovyShell shell = new GroovyShell(binding);

					Object value = shell.evaluate(configFileContents);
					// Returned value should be the item to save
					if (value instanceof Item){
					
						log.debug "Saving item ...."
						if(!item.save())  {
				   	    	item.errors.allErrors.each {
				   	      		log.error it
				   	   		}
				   		} 
						
						def total = (System.currentTimeMillis()-itemStartTime)/1000
						log.info "${total} seconds to save ${item}"
						itemStartTime=0
						if (count % 50 == 0) cleanUpGorm()

						
				   log.debug "*************************\n"
				   ++count
				   
				   // reset the item pointer
				   item = null
				   
				   // reset the metadata hash
				   m.clear()
					
					} else {
						log.error "Error - incorrectly configured config script. It should return an instance of ${Item.class}"
						System.exit(1);
					}

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
   		}

        // Now reset the character buffer
        buffer = ""
    }
    

	
	
	def cleanUpGorm() {
		def session = sessionFactory.currentSession
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



def usage(){
	println "Usage: IndexMetadata <config> <oai-pmh file>"
	System.exit(1)
}





   /*
	Need to work out how we were called as this depends which variable contains the command line arguments
	
	- If we were executed directly by the groovy CLI eg. groovy <script name> 
	then the command line aguments can be accessed via 'args'
	
	- If however we were called by using grails RunScript <script name>
	then the command line arguments wil be parsed by the Grilas RunScript script and stored in 
	the array getBinding().variables.get('args') (but the first element will be the path to this script)
*/



def tokens

def hasArgsParser = getBinding().variables.containsKey('argsMap')
if (hasArgsParser) {
    println "Called from grails RunScript ... using args stored in getBinding().variables.get('args')"
    println "Parser args found - they are: ${getBinding().variables.get('args')}"
    
    String scriptArgs = ""
    // Need to strip off script name 
    getBinding().variables.get('args')?.tokenize().eachWithIndex() { obj, i -> if (i > 0) scriptArgs += obj + " "}
    
    println "Script args are: ${scriptArgs}"
    tokens = scriptArgs.tokenize()
}
else {
  	// called directly for example using Groovy command line
  	// The script cannot be invoked this way - throw error. It must be executed under Grails so that
  	// domain classes are metaprogrammed via GORM
  	println "Error: Script must be executed via a grails run-script command. The script should not be executed directly"
  	System.exit(1)
}


    println "Command line arguments: ${tokens} ${tokens.class}"
    
    if (tokens.size() != 2){
    	usage()
    }
    
    else {
    	println "Config file is: ${tokens.get(0)}"	
    	println "Parsing OAI-PMH file: ${tokens.get(1)}"
    
    	String configFileText = new File(tokens.get(0)).text;
    
    	println "*************************"
		println "*"
		println "* Config file contents:"
		println "*"
		println "*************************"
    	println "${configFileText}"
		println "*************************"
		println "*"
		println "* End config"
		println "*"
		println "*************************"
    
   		def f = new File(tokens.get(1))
    
    	def count = 0
       
    	def handler = new OAIHandler(configFileContents: configFileText)
    	def reader = SAXParserFactory.newInstance().newSAXParser().XMLReader
  		
  		println "XMLReader class is ${reader.class}"
  		reader.setFeature("http://xml.org/sax/features/namespaces", true)
  		reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true)
  		
  		
  		reader.setContentHandler(handler)
  		def inputStream = new FileInputStream(f)
		def indexStartTime = System.currentTimeMillis()
  		reader.parse(new InputSource(inputStream))
		def totalTime = (System.currentTimeMillis() - indexStartTime)/1000

		
		println "Indexed ${handler.getCount()} Items in ${totalTime} seconds (${totalTime/60} minutes) "
    
	}
   