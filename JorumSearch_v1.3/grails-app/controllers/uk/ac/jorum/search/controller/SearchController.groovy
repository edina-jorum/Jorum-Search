package uk.ac.jorum.search.controller

import org.apache.log4j.*
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrQuery.ORDER
import org.grails.solr.SearchResults

import org.apache.solr.common.params.ModifiableSolrParams
import grails.converters.JSON
import uk.ac.jorum.search.domain.Item
import uk.ac.jorum.search.domain.Subject
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class SearchController {
	
	static Logger log = Logger.getInstance(SearchController.class)
	
	static List maxResultsPerPage = [10, 20, 50, 100]
	
	public enum SortCriteria{
	
		RELEVANCE_DESC("Relevance Descending", "score", false),
		RELEVANCE_ASC("Relevance Ascending", "score", true),
		DATE_DESC("Date Descending", "oai_datestamp", false),
		DATE_ASC("Date Ascending", "oai_datestamp", true),
		TITLE_DESC("Title Descending", "meta_title", false),
		TITLE_ASC("Title Ascending", "meta_title", true);
	
		String name
		String solrField
		int id
		boolean ascending
		
		SortCriteria(String name, String solrField, boolean ascending){
			this.name = name
			this.solrField = solrField
			this.ascending = ascending
		}	
	
		int getId(){
			return this.ordinal()
		}
		
	
		public static SortCriteria findById(Object id){
			SearchController.log.debug("SortCriteria:find ${id} of type ${id.getClass()}")
			SortCriteria result = null
			
			if (id != null){
				for (SortCriteria s:SortCriteria.values()){
					if ((id instanceof String) && (s.id == Integer.parseInt((String)id))){
						result = s
						break
					} 
					
					if ((id instanceof Integer) && (s.id == ((Integer)id))){
						result = s
						break
					} 
				}
			}
			
			SearchController.log.debug("SortCriteria:find returning ${result}")
			return result
		}
	
	}
	
	
	
	
	
	LinkedHashMap doQuery(params) {
		
		SortCriteria sortCriteria = SortCriteria.findById(params['sortSelection'])
		List fq = []
		
		if (params.q == "") params.q="*"
		
		def query = new SolrQuery("${params.q}")
		
		if(params.fq) {
			query.addFilterQuery(params.fq)
			if(params.fq instanceof String)
			fq << params.fq
			else 
			fq = params.fq
		}
		if(params.offset)
		query.setStart(params.offset as int)
		if(params.max){
			// If someone has tried to enter a param manually, we ignore it if it's not in the expected values
			// and set a default of 10
			params.max = this.maxResultsPerPage.contains(params.max as int) ? params.max : 10
			query.setRows(params.max as int)
		}

		query.facet = true
		["subjects", "meta_rights", "publishers", "creators", "repository_name"].each {
			query.addFacetField(Item.solrFieldName(it))
		}
		query.setFacetMinCount(CH.config.FacetMinCount)
		query.setFacetLimit(CH.config.FacetLimit)
		
		log.debug("doQuery: sort criteria is ${sortCriteria}")
		if (sortCriteria != null){
			query.setSortField(Item.solrFieldName(sortCriteria.solrField), (sortCriteria.ascending)?ORDER.asc:ORDER.desc)
		}
		
		
		def result = [:]
		
		log.debug("doQuery: Sending query to solr: ${query}");
		result['results'] = Item.searchSolr(query)
		result['fq'] = fq
		result['query'] = query

		return result
	}
	
	
	def index = {
		
		def queryResults = doQuery(params)
		def feedLink = createLink(controller:'search',action:'feed',params: params)
		
		
		
		
		[result:queryResults['results'], q:params.q, fq: queryResults['fq'], solrQueryUrl: queryResults['query'].toString(), feedLink: feedLink, sortCriteria:SortCriteria.values(), sortSelection:params['sortSelection']]
	}
	
	
/*	def autoCompleteJSON = {
//Leaving this in for the time being - the grailsui plugin for autocomplete used this implementation
// Also shows how to get the same information from solr
//		def list =Item.searchSolr(params.query).resultList
//		list.each() { item ->
//			println "BEGIN***********";
//			println "The title is ${item.title}"
//			println "The id is ${item.id}"
//			item.each{println it}
//			println "END***********";
//		 };
//		 println "";
		
		def list = Item.findAllBymeta_titleLike("${params.query}%",  [max: 10])
		list.each() { item -> 
			print " ${item.id}"
			print " ${item.meta_title}"
		}
		def jsonList = list.collect { [ id: it.id, name: it.meta_title ]
		}
		
		jsonList.each {println it
		}
		def jsonResult = [
		result: jsonList
		]
		render jsonResult as JSON
	}*/
	
	def autoComplete = {
		//The richui autocomplete plugin requires xml with the item id and title
		def list = Item.findAllBymeta_titleLike("${params.query}%",  [max: 10])
		render(contentType: "text/xml") {
			results() {
				list.each { item ->
					result() { 
						id(item.id)
						name(item.meta_title)
					}
				}
			}
		}
	}
	
	
	def feed = {
		def queryResults = doQuery(params)

		render(feedType:"rss", feedVersion:"2.0") {
            title = "JorumSearch Feed"
            description = "JorumSearch Feed"
            
            log.debug("feed: Creating link for RSS feed...")
            log.debug("feed: params is ${params}")
            link = createLink(controller:'search', action:'feed', params: params, absolute:true)
            log.debug("feed: Created link ${link}")
            
            queryResults['results'].resultList.each(){ item ->

            	entry(item.meta_title) {
                    link = item.meta_identifier_uri
                    item.meta_description // return the content
                }
            
            
            }
            
            
        }
	
	
	}
	
}
