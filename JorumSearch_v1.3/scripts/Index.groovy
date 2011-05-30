import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

//Map containing target name (see classes in uk.ac.jorum.search.parse.target) and associated OAI-PMH harvest file
def targetMap=CH.config.grails.index.targetMap
println "The targets that will be parsed are ${targetMap}"

// Get the Index service
def indexService = AH.application.mainContext.getBean("IndexMetadataService");

def indexStartTime = System.currentTimeMillis()
//Iterate over targets
targetMap.each{ key, value ->
	println "About to store ${key}"
	indexService.index(key, value)
}
// all items have been written to the db, now send batches to solr and finally commit
indexService.submitBatchesToSolr()

def indexStopTime = System.currentTimeMillis()

println "Indexing for:"
targetMap.keySet().each{println it}
println "is complete and took ${((indexStopTime-indexStartTime)/1000)/60} minutes"