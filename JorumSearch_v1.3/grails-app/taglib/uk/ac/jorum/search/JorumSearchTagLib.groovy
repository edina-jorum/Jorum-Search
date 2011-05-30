package uk.ac.jorum.search

class JorumSearchTagLib {
	def keywords = { attrs, body ->
		
		def facet = attrs.fq
		def subjects = attrs.item.subjects
		def tags
		if (subjects instanceof java.lang.String)
			tags = subjects.split(',') 
		else
			tags = subjects
		
		attrs.item.subjects=tags
		
		out << """<ul class="keywords">Keywords:"""
		
		tags.each { tag, fq=facet ->
			tag = tag.trim()
			
			out << "<li>\n" 
			out << "<a href='"
			out << "${createLink(controller:'search',action:'index',params: ['q':tag, 'fq':fq])}'" 
			out << """title="Show all resources related to the keyword ${tag}"/>"""
			out << "${tag}"
			out << "</a>" 
			out << "</li>\n"

		}	
		out << "</ul>"
		
	}
	
	def resultsTotalTitle = { attrs, body ->
		
		def result = attrs.result
		def offset = attrs.offset
		def max    = attrs.max
		def start
		def end
		
		try 
		{
			start = params.offset.toInteger() + 1
		} 
		catch (NullPointerException e) 
		{
			start = 1
		}
		
		try 
		{
			offset = params.offset.toInteger()
		} 
		catch (NullPointerException e) 
		{
			offset = 0
		}
		
		try 
		{
			max = params.max.toInteger()
		} 
		catch (NullPointerException e) 
		{
			if (result.total < 10)
				max = result.total
			else
				max = 10
		}

		if ((result.total-offset) < max)
			end = result.total
		else
			end = offset + max
		
		out << "<div id=\"feedback\" class=\"info\">\n"
		out << "\t<p>Got something you'd like to share with us about Jorum Search? Feel free to <a href=\"http://www.jorum.ac.uk/feedback\" title=\"Send feedback\">send us your feedback</a>.</p>\n"
		out << "</div>\n"
		out << """<h2>Displaying items ${start} to ${end} of ${result.total}</h2>"""
		
	}
}