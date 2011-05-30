package uk.ac.jorum.search.domain

class Publisher {

    static constraints = {
    	
    	value(blank:true, nullable: false, maxSize: 1024)
    }
    
    String value
    String toString() { value }
  
    
    static belongsTo = [item:Item]
    
}
