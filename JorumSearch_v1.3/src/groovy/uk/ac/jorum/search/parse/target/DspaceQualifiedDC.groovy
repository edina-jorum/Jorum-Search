package uk.ac.jorum.search.parse.target

import org.apache.log4j.*
import uk.ac.jorum.search.domain.*;

name = "Closure to parse qualified DC output from JorumOpen target"
key = "DspaceQualifiedDC"


def log = Logger.getInstance(DspaceQualifiedDC.class)

parse = { metaHash, item->
	
	log.debug "*************************"
	log.debug "*"
	log.debug "* Inside config handler for Qualified DC DSpace output"
	log.debug "*"
	log.debug "*************************"
	
	log.debug "Metadata hash passed in is:"
	log.debug "${metaHash}"
	
	item.repository_name="Jorum Open"
	
	item.meta_title = metaHash['title']?.get(0)
	item.meta_identifier = metaHash['identifier']?.get(0)
	item.meta_identifier_uri = metaHash['identifier.uri']?.find{it =~ /^https?:\/\//}
	
	item.meta_description = metaHash['description']?.get(0)
	item.meta_rights = metaHash['rights']?.get(0)
	item.meta_rights_uri = metaHash['rights.uri']?.get(0)
	
	item.meta_date_available = metaHash['date.available']?.get(0)
	item.meta_date_created = metaHash['date.created']?.get(0)
	item.meta_date_issued = metaHash['date.issued']?.get(0)
	
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
		item.addToSubjects(value:it);
	}
	
	// Must reset the list before adding the values read from the harvest to prevent duplicates
	item.publishers = []
	metaHash['publisher']?.each() {
		item.addToPublishers(value:it);
	}
	
	// Must reset the list before adding the values read from the harvest to prevent duplicates
	item.creators = []
	metaHash['creator.author']?.each() {
		item.addToCreators(value:it);
	}
	// Creators may also be expressed by dc:creator elements
	metaHash['creator']?.each() {
		item.addToCreators(value:it);
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

