package uk.ac.jorum.search.domain

class Creator {

	static constraints = {
    	value(blank:false, nullable: false, maxSize: 1024)
    }
    
    String value
    String toString() { value }
  
    
    static belongsTo = [item:Item]
}
