grails.server.port.http=20000
grails.server.port.https=8443
grails.server.host=null
grails.app.context="/"
grails.serverURL="http://localhost:${grails.server.port.http}/"
//Map containing target name (see classes in uk.ac.jorum.search.parse.target) and associated OAI-PMH harvest file
//grails.index.targetMap = ["DspaceQualifiedDC":"/home/jorum/dist/oai-pmh-harvester/harvests/jorumOpen.xml"]
grails.index.targetMap = ["DspaceQualifiedDC":"/pathToOAIoutput/file.xml"]
