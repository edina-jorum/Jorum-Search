package uk.ac.jorum.search.domain

import org.grails.solr.Solr

class Item {

	// To enable indexing of a domain class include this static declaration, doing so will enable the dynamic methods on the domain object. 
	static enableSolrSearch = true
	
	//To have the index updated on inserts, updates and deletes of your domain classes, declare this property in your domain
//	static solrAutoIndex = true
	
	// OAI-PMH Elements
	String oai_identifier
	Date oai_datestamp
	
	// names match the DC element names with prefix meta_, qualifiers in format name_qualifier (cannot use dot)

	String meta_title
	String meta_identifier
	String meta_identifier_uri
	
	@Solr(asTextAlso=true)
	String meta_description
	
	String meta_language
	String meta_date_available
	String meta_date_issued
	String meta_date_created
	String meta_format
	String meta_rights
	String meta_rights_uri
	
	String repository_name
	
	// Transient field (not persisted to db) - used at index time to mark items for deletion
	Boolean toBeDeleted
	
	static hasMany = [subjects:Subject, publishers:Publisher, creators:Creator]
	
	
	Date dateCreated //Note: Automatically filled in by Grails when instance written to DB
	Date lastUpdated //Note: Automatically filled in by Grails when instance written to DB
	
	String toString() { meta_title }
	
	
    static constraints = {
    	oai_identifier(blank:false, nullable: false)
		repository_name(blank:false, nullable: false)
    	oai_datestamp(blank:false, nullable: false)
    	meta_title(blank:false, nullable: false, maxSize: 1024)
    	meta_identifier(blank:true, nullable: true)
    	meta_identifier_uri(blank:true, nullable: true, maxSize: 1024)
    	meta_description(blank:true, nullable: true, maxSize: 5120)
    	meta_language(blank:true, nullable: true)
    	meta_date_available(blank:true, nullable: true)
    	meta_date_issued(blank:true, nullable: true)
    	meta_date_created(blank:true, nullable: true)
    	meta_format(blank:true, nullable: true)
    	meta_rights(blank:true, nullable: true)
    	meta_rights_uri(blank:true, nullable: true)
    	
    }
	
	static mapping = {
		//Add an index on the oai_identifier field - db is queried on this field at index time for each record.
        oai_identifier index:'oai_identifier_Idx'
    }

	
	static transients = ['toBeDeleted']
	
  
	
}
