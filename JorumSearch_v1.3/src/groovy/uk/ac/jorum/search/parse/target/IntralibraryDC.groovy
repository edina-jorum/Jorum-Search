package uk.ac.jorum.search.parse.target

import org.apache.log4j.*
import uk.ac.jorum.search.domain.*;

name = "Closure to parse  DC output from JorumUK target"
key = "IntralibraryDC"


def log = Logger.getInstance(IntralibraryDC.class)

parse = { metaHash, item->
	
	log.debug "*************************"
	log.debug "*"
	log.debug "* Inside config handler for Intralibrary output"
	log.debug "*"
	log.debug "*************************"
	log.debug "Metadata hash passed in is:"
	log.debug "${metaHash}"
	
	item.repository_name ="Jorum UK"
	
	item.meta_title = metaHash['title']?.get(0)
	item.meta_identifier = metaHash['identifier']?.get(0)
	
	item.meta_identifier_uri = metaHash['identifier']?.find{it =~ /^https?:\/\//}
	
	item.meta_description = metaHash['description']?.get(0)
	/*item.meta_rights = metaHash['rights']?.get(0)
	 item.meta_rights_uri = (metaHash['rights'] && metaHash['rights'].size() > 1) ? metaHash['rights']?.get(1) : ""*/
	
	item.meta_rights = "JorumUK Institutional Licence"
	item.meta_rights_uri = "http://www.jorum.ac.uk/licences/JORUM_RepurposeNoRepublishTandCv1p0.html"
	
	item.meta_date_available = (metaHash['date'] && metaHash['date'].size() > 1) ? metaHash['date']?.get(1) : metaHash['date']?.get(0)
	item.meta_date_created = (metaHash['date'] && metaHash['date'].size() > 2) ? metaHash['date']?.get(2) : metaHash['date']?.get(0)
	item.meta_date_issued = (metaHash['date'] && metaHash['date'].size() > 3) ? metaHash['date']?.get(3) : metaHash['date']?.get(0)
	
	item.meta_language = metaHash['language']?.get(0)
	
	item.meta_format = metaHash['format']?.get(0)
	
	
	/*
	 * Setup Subjects, Publisher and Creator
	 *
	 * NOTE: Must do before saving to ensure referential integrity
	 */
	// Must reset the list before adding the values read from the harvest to prevent duplicates
	item.subjects = []
	metaHash['subject']?.each() {
		if(it.length()!=0){
			item.addToSubjects(value:it);
		}
	}
	
	
	
	
	
	/*
	 * Closure which takes a string containing a vCard,
	 * converts it into a map and returns.  Note that this 
	 * implementation relies on the vCard having a new line after
	 * every entry
	 */
	def vCardToMapClosure = {obj ->
		def map = [:]
		obj.findAll(/([A-Z]*):([^\n]*)/) { full, name, value -> 
			map[name] = value
		}
		log.debug "vcard map: "
		map.eachWithIndex() { v, i -> log.debug " ${i}: ${v}" };
		return map
	}
	
	/*
	 * Closure that adds a vCard field to an item.
	 * field: the identifier of the field to be added
	 * vcardMap: the contents of the vCard as a map
	 * method: String which is evaluated to a method name which
	 * will be called on item.
	 */
	def addVCardEntryToItemClosure = {field, vcardMap, method->
		def value = vcardMap.get(field)
		if (value){
			item."$method"(value: value)
			log.debug "added "+value +" successfully"
		}else{
			log.debug "Could not find an entry for "+field
		}
	}
	
	/*
	 * 	Closure which takes a vcardString, a methodName string (which will be
	 *  invoked on the item class dynamically) and a list of the fields we 
	 *  wish to pick out of the vcard
	 */
	def processVCardClosure = { vcardString, methodName, listFields ->
		def vCardMap = vCardToMapClosure(vcardString)
		if (vCardMap){
			// add the values defined for FN and ORG as creators
			listFields.each{addVCardEntryToItemClosure(it, vCardMap, methodName)}
		}else{
			log.debug vcardString +" does not appear to be a vCard so just adding directly"
			item."$methodName"(value: vcardString)
		}
	}
	
	// Must reset the list before adding the values read from the harvest to prevent duplicates
	item.publishers = []
	metaHash['publisher']?.each() { vcardString ->
		processVCardClosure(vcardString,"addToPublishers", ["FN","ORG"])
	}
	
	// Must reset the list before adding the values read from the harvest to prevent duplicates
	item.creators = []
	metaHash['creator']?.each() { vcardString ->	
		processVCardClosure(vcardString,"addToCreators", ["FN","ORG"])
	}
	
	log.debug "Returning item:"
	log.debug "${item}"
	
	
	log.debug "*************************"
	log.debug "*"
	log.debug "* Leaving config handler"
	log.debug "*"
	log.debug "*************************"
	
	return item
}

