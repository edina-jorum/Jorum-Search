import uk.ac.jorum.search.Role
import uk.ac.jorum.search.User
import uk.ac.jorum.search.UserRole

class BootStrap {
	
	//inject the index service
	def IndexMetadataService
	
	// inject the Spring Security Service
	def springSecurityService
	
	def init = { servletContext ->
		
		/*
		 * Add Spring Security Service admin user and role
		 */
		
		String password = springSecurityService.encodePassword('changeme')
		
		def adminUser = new User(username: 'admin', enabled: true, password: password)
		adminUser.save(flush: true)
		
		def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
		//def userRole = new Role(authority: 'ROLE_USER').save(flush: true)

		UserRole.create adminUser, adminRole, true
		
		/*
		 * NOTE:  This doesn't seem to get called when we're running the index task via RunScript.  It gets called ok 
		 * when we do grails run-app.  I've tried to invoke it via RunScript to no avail.  In the meantime it is invoked
		 * in IndexMetadataService in a messy fashion...
		 * 
		 */
		
		// On startup we want to populate the list of oai parsers and also create the getSolrDocument metamethod 
		// for the domain classes, which returns a Solr document for a particular domain instance.
	
		IndexMetadataService.refreshTargets()
		IndexMetadataService.createSolrDocumentDomainMethod()
		
	}
	def destroy = {
	}
} 