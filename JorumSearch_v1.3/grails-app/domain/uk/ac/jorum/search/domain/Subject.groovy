package uk.ac.jorum.search.domain

import org.grails.solr.Solr

class Subject {

    static constraints = {
    	value(blank:false, nullable: false, maxSize: 1024)
    }
    
    String value
    String toString() { value }
  
    
    static belongsTo = [item:Item]
}
