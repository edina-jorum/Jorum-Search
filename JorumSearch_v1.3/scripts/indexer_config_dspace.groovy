log.debug "*************************"
log.debug "*"
log.debug "* Inside config handler for DSpace output"
log.debug "*"
log.debug "*************************"

log.debug "Metadata hash passed in is:"
log.debug "${metaHash}"

item.repository_name="Jorum Open"

item.meta_title = metaHash['title']?.get(0)
item.meta_identifier = metaHash['identifier']?.get(0)

// Identifier URI
/*if (metaHash['identifier']){
	if (metaHash['identifier'].size() > 1){
		item.meta_identifier_uri = metaHash['identifier']?.get(1) 
	} else {
		if (metaHash['identifier'].get(0).startsWith("http://") || metaHash['identifier'].get(0).startsWith("https://")){
			item.meta_identifier_uri = metaHash['identifier'].get(0)
		}
	}
}*/
item.meta_identifier_uri = metaHash['identifier']?.find{it =~ /^https?:\/\//}
		
item.meta_description = metaHash['description']?.get(0)
item.meta_rights = metaHash['rights']?.get(0)
item.meta_rights_uri = (metaHash['rights'] && metaHash['rights'].size() > 1) ? metaHash['rights']?.get(1) : ""

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
	item.addToSubjects(value:it);
}

// Must reset the list before adding the values read from the harvest to prevent duplicates
item.publishers = []
metaHash['publisher']?.each() {
	item.addToPublishers(value:it);
}

// Must reset the list before adding the values read from the harvest to prevent duplicates
item.creators = []
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


