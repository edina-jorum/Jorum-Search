<%@page import="uk.ac.jorum.search.domain.Item"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xmlns:dri="http://di.tamu.edu/DRI/1.0/" xmlns:i18n="http://apache.org/cocoon/i18n/2.1">

<head>
	<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
	
	<title>Jorum Search</title>
	
	<resource:autoComplete skin="default" />
	<g:if test="${q}"><link rel="alternate" type="application/rss+xml" title="RSS 2.0" href="${feedLink}"/></g:if>
	<script type="text/javascript" charset="utf-8">
		function SubmitSearch(e) 
		{ 
		    var searchForm = document.getElementById("search");
			searchForm.submit();
		} 

		YAHOO.util.Event.addListener("num-results", "change", SubmitSearch);
		YAHOO.util.Event.addListener("sort-criteria", "change", SubmitSearch);
	</script>
	
	<link type="image/x-icon" href="${resource(dir: '/')}images/favicon.ico" rel="icon" />
	<link type="image/x-icon" href="${resource(dir: '/')}images/favicon.ico" rel="shortcut icon" />
	<link type="text/css" rel="stylesheet" media="screen" href="${resource(dir: '/')}css/960.css" />
	<link type="text/css" rel="stylesheet" media="screen" href="${resource(dir: '/')}css/reset.css" />
	<link type="text/css" rel="stylesheet" media="screen" href="${resource(dir: '/')}css/text.css" />
	<link type="text/css" rel="stylesheet" media="screen" href="${resource(dir: '/')}css/style.css" />
	
	<link type="text/css" rel="stylesheet" media="screen" href="http://resources.jorum.ac.uk/xmlui/themes/Jorum_v2/lib/style.css">
	
	<!--[if IE 6]>
	<link type="text/css" rel="stylesheet" media="screen" href="/xmlui/themes/Jorum_v2/lib/style-ie6.css" />
	<![endif]-->
	
</head>


<body>
	<div class="container_12" id="pageBody">
		<!--<div id="header">
			<h1 class="grid_6">
				<a href="http://www.jorum.ac.uk"><img id="getstarted" alt="Jorum: Learning to Share" src="${resource(dir: '/')}images/jorumMainLogo.jpg" /></a>
				<img alt="Jorum: Learning to Share" src="${resource(dir: '/')}images/jorumLearntoShare.jpg" />
			</h1>
		</div>
		<br class="clear" />
		<ul id="mainmenu" class="grid_12">
			<li style="padding-left:0;"><a id="homenav" href="http://www.jorum.ac.uk">Home</a></li>
			<li><a id="getnav" href="http://www.jorum.ac.uk/getstarted/getstarted.html">Get Started</a></li>
			<li><a id="commnav" href="http://community.jorum.ac.uk">Community</a></li>
			<li><a id="supportnav" href="http://www.jorum.ac.uk/support/support.html">Support</a></li>
			<li><a id="newsnav" href="http://jorumnews.blogspot.com/">News</a></li>
			<li><a id="aboutnav" href="http://www.jorum.ac.uk/aboutus/index.html">About Us</a></li>
			<li style="border-right:none;"><a id="depositnav" href="http://www.jorum.ac.uk/deposit/deposit.htm">Deposit</a></li>
		</ul>-->
		<div id="header">
			<h1 class="grid_6">
			<a href="http://www.jorum.ac.uk">
			 <img id="getstarted" alt="Jorum: Learning to Share" src="http://resources.jorum.ac.uk/xmlui/themes/Jorum_v2/images/jorumMainLogo.jpg">
			</a>
			<img alt="Jorum: Learning to Share" src="http://resources.jorum.ac.uk/xmlui/themes/Jorum_v2/images/jorumLearntoShare.jpg">
			</h1>
<!--<div id="main-search">
<form accept-charset="utf-8" action="http://resources.jorum.ac.uk/xmlui/search" method="post" id="main-search-form">
<div style="display: none;">
  <input value="POST" name="_method" type="hidden">
</div>
<div class="input text">
<label for="SearchQuery">
Query
</label>
  <input id="SearchQuery" value="Search learning &amp; teaching resources" name="query" class="input" type="text">
</div>
<div class="submit">
  <input value="Search" type="submit">
</div>
</form>
</div>-->
		</div>

		<br class="clear" />
		<ul id="mainmenu" class="grid_12">
		<li style="padding-left:10px;">
		<a href="http://www.jorum.ac.uk/">Home</a>
		</li>
		<li>
		<a class="_active" href="http://resources.jorum.ac.uk/xmlui">Find</a>
		</li>
		<li>
		
		<a href="https://vsp2.edina.ac.uk/target/ukfedLogin/jorumdspace_live?service=jorum,jorumdepositor">Share</a>
		</li>
		<li>
		<a href="http://community.jorum.ac.uk/">Discuss</a>
		</li>
		<li>
		<a href="http://jorumnews.blogspot.com/">News</a>
		</li>
		<li>
		<a href="http://www.jorum.ac.uk/help">Help</a>
		</li>
		</ul>


		<div class="clear"></div>
		<p class="grid_12" id="breadcrumb">
			<g:if test="${!q}">
			You are here: Jorum Search Home
			</g:if>
			<g:else>
			You are here: <a href="${resource(dir: '/')}" title="Jorum Search Home">Jorum Search Home</a> &raquo; Search Results
			</g:else>
		</p>
		<div class="grid_12" id="big_content">
	<g:if test="${!q}">
		<div id="badge-index">
			<img src="${resource(dir: '/')}images/beta-badge.png" class="badge_png" alt="Beta" />
		</div>
		<div id="search-box" class="grid_4 push_4">
        <!-- <h1>Search Front</h1> -->
		<img src="${resource(dir: '/')}images/jorumMainLogo.jpg" alt="Jorum Logo" />
		<g:form name="search" action="index" method="get">

			 <richui:autoComplete 
			 	id="q"
				name="q"  
				action="${resource('dir': 'search/autoComplete')}" 
				maxResultsDisplayed="10"
				autoHighlight="false"
			 />

        <g:submitButton name="search" value="Search" id="search-button" />
		<g:hiddenField name="max" value="20"/>

        </g:form>
		</div>
		<br class="clear" />
		<div id="feedback" class="info front-info">
			<p>Got something you'd like to share with us about Jorum Search? Feel free to <a href="http://www.jorum.ac.uk/feedback" title="Send feedback">send us your feedback</a>.</p>
		</div>
		
	</g:if>
	
	<g:if test="${result.total}">
		<div id="side_menu" class="grid_3">
            <g:form name="search" action="index" method="get">

		        <fieldset id="search-fieldset">
		        	<legend>Search</legend>
					<richui:autoComplete 
				 		id="q"
						name="q"  
						action="${resource('dir': 'search/autoComplete')}" 
						maxResultsDisplayed="10"
						value="${q}"
						autoHighlight="false"
					 />
					<g:submitButton name="search" value="Search"  id="search-button"/>
					<fieldset id="search-options">
						<legend>Display Options</legend>
						<p>Results per page: <g:select name="max" id="num-results" from="${[10, 20, 50, 100]}" value="${params.max}" /></p>
						<p>Sort by: <g:select name="sortSelection" id="sort-criteria" from="${sortCriteria}" optionKey="id" optionValue="name"  value="${sortSelection}" /></p>
					</fieldset>
		        </fieldset>
				<%
					fq.each { facet ->
						out << """<input type="hidden" name="fq" value="${facet}" />"""
					}
				%>
	        </g:form>
		
            <solr:facet field="${Item.solrFieldName('repository_name')}" result="${result}" fq="${fq}" q="${q}" min="2">
                <h3>Filter by Repository</h3>
            </solr:facet>

			<solr:facet field="${Item.solrFieldName('meta_rights')}" result="${result}" fq="${fq}" q="${q}" min="1">
                <h3>Filter by Rights</h3>
            </solr:facet>
	     </div>
	</g:if>
			<div id="second_level_content" class="grid_9">
	        <g:if test="${result.total}">
				<div id="badge">
					<img src="${resource(dir: '/')}images/beta-badge.png" class="badge_png" alt="Beta" />
				</div>
				<g:resultsTotalTitle result="${result}" offset="${params.offset}" max="${params.max}" />
				<g:each in="${result.resultList}" status="i" var="item">
	            <div class="record ${(i % 2) == 0 ? 'odd' : 'even'}">
					<%
					switch(item.meta_rights_uri)
					{
						case "http://creativecommons.org/licenses/by/2.0/uk/":
							cc_small_img_url = "http://upload.wikimedia.org/wikipedia/commons/b/b1/CC-BY-icon-80x15.png"
							cc_big_img_url   = "http://upload.wikimedia.org/wikipedia/commons/5/5b/Cc-by-icon.png"
							break;
						case "http://creativecommons.org/licenses/by-sa/2.0/uk/":
							cc_small_img_url = "http://upload.wikimedia.org/wikipedia/commons/9/96/CC-BY-SA-icon-80x15.png"
							cc_big_img_url   = "http://upload.wikimedia.org/wikipedia/commons/f/f3/CC-BY-SA_3_icon_88x31.png"
							break;
						case "http://creativecommons.org/licenses/by-nc/2.0/uk/":
							cc_small_img_url = "http://upload.wikimedia.org/wikipedia/commons/b/bc/CC-BY-NC-icon-80x15.png"
							cc_big_img_url   = "http://upload.wikimedia.org/wikipedia/commons/d/da/CC-BY-NC-icon-88x31.png"
							break;
						case "http://creativecommons.org/licenses/by-nc-sa/2.0/uk/":
							cc_small_img_url = "http://upload.wikimedia.org/wikipedia/commons/1/1f/CC-BY-NC-SA-icon-80x15.png"
							cc_big_img_url   = "http://upload.wikimedia.org/wikipedia/commons/7/7e/CC-BY-NC-SA-icon-88x31.png"
							break;
						case "http://creativecommons.org/licenses/by-nd/2.0/uk/":
							cc_small_img_url = "http://upload.wikimedia.org/wikipedia/commons/8/8e/CC-BY-ND-icon-80x15.png"
							cc_big_img_url   = "http://upload.wikimedia.org/wikipedia/commons/d/d7/CC-BY-ND-icon-88x31.png"
							break;
						case "http://creativecommons.org/licenses/by-nc-nd/2.0/uk/":
							cc_small_img_url = "http://upload.wikimedia.org/wikipedia/commons/0/03/CC-BY-NC-ND-icon-80x15.png"
							cc_big_img_url   = "http://upload.wikimedia.org/wikipedia/commons/4/4c/CC-BY-NC-ND-icon-88x31.png"
							break;
						case "http://www.jorum.ac.uk/licences/JORUM_RepurposeNoRepublishTandCv1p0.html":
							cc_small_img_url = ""
							cc_big_img_url   = "${resource(dir: '/')}images/jorumuk-licence-big.png"
							break;
					}		
					%>
					<div class="rights">
						<a href="${item.meta_rights_uri}" title="${item.meta_rights}" class="rights-image">
							<img src="${cc_big_img_url}" alt="${item.meta_rights_uri}" />
						</a> 
						<a href="${item.meta_rights_uri}" title="${item.meta_rights}" class="rights-text">
							${item.meta_rights}
						</a>
					</div>
			
					<h3>
						<a href="${item.meta_identifier_uri}" title="Permanent link for ${item.meta_title}"><img src="${resource(dir: '/')}images/icons/world_link.png" width="16" height="16" alt="Permanent link for ${item.meta_title}" /></a>
						<a href="${item.meta_identifier_uri}" title="Permanent link for ${item.meta_title}">${item.meta_title}</a>
					</h3>
					<p>
						${item.meta_description}
					</p>
					<% 
						if (item.creators instanceof java.lang.String)
							authors = item.creators.split(',')
						else
							authors = item.creators
					%>
					<ul class="authors">Authors:
					<g:each in="${authors}" var="author">
						<% 
							author = author.trim()
						%>
						<li><a href="<g:createLink action="index" params="[q:author,fq:fq]" />" title="Show all resources related to the author ${author}">${author}</a></li>
					</g:each>
					</ul>
		
					<g:keywords item="${item}" fq="${fq}" />
		
				</div>
	        	</g:each>
        
		        <br/>
		
				<span class="paginate">
		            <g:paginate action="index" total="${result.total}" max="${max}" params="[q:q, fq:fq]"/>
		        </span>
			</g:if>
		    <g:if test="${result.total == 0}">
				<g:if test="${q}">
	        	<h3>No items found</h3>
				<p>Would you like to <a href="${resource(dir: '/')}">try another search?</a></p>
				</g:if>
	        </g:if>
		</div>
	</div>
	<div style="float:left; background-color:#eeeeee; padding: 10px; display:none">
        <h3>Raw Solr URL Parameters</h3>
        <g:each in="${solrQueryUrl.split('&')}">
            ${it.decodeURL()}<br/>
        </g:each>
    </div>
	<br class="clear" />
<!--	<div id="footer1" class="grid_12">
		<ul class="grid_6 alpha" id="footmenu">
			<li><a href="http://www.jorum.ac.uk/termsofservice.html">Terms of Service</a></li>
			<li><a href="http://www.jorum.ac.uk/website_help.html">Website Help</a></li>
			<li><a href="http://www.jorum.ac.uk/policies.html">Policies</a></li>
			<li><a href="http://www.jorum.ac.uk/sitemap.html">Site Map</a></li>
		</ul>
		<div id="mailbox">
			<a href="http://www.jorum.ac.uk/support/feedback.php">
			<img alt="Email us your feedback" style="float:right;" src="${resource(dir: '/')}images/mailicon.gif" />Feedback</a>
		</div>
	</div>
	
	<br style="margin-bottom:10px;" class="clear" />
	
	<div id="jisc" class="grid_6">
		<p><img alt="JISC logo" src="${resource(dir: '/')}images/jisc.gif" /></p>
	</div>
	
	<div id="edina-mimas" class="grid_6">
		<p><img alt="Joint Edina and Mimas data centres logo" src="${resource(dir: '/')}images/edina_mimas.gif" /></p>
	</div>-->
	
	<div id="footer1" class="grid_12">
		<ul class="grid_6 alpha" id="footmenu">
			<li>
			<a href="http://www.jorum.ac.uk/about-us">About us</a>
			</li>
			<li>
			<a href="http://www.jorum.ac.uk/terms-of-service">Terms of Service</a>
			</li>
			
			<li>
			<a href="http://www.jorum.ac.uk/Policies">Policies</a>
			</li>
			<li>
			<a href="http://www.jorum.ac.uk/sitemap">Site Map</a>
			</li>
		</ul>
		<div id="mailbox">
			<a href="http://www.jorum.ac.uk/feedback">
			<img alt="Email us your feedback" style="float:right;" src="http://resources.jorum.ac.uk/xmlui/themes/Jorum_v2/images/mailicon.gif" />Feedback</a>
		</div>
		</div>
		<br style="margin-bottom:10px;" class="clear" />
		<div id="jisc" class="grid_6">
		
		<p>
		<img alt="JISC logo" src="http://resources.jorum.ac.uk/xmlui/themes/Jorum_v2/images/jisc.gif" />
		</p>
		</div>
		<div id="edina-mimas" class="grid_6">
		<p>
		<img alt="Joint Edina and Mimas data centres logo" src="http://resources.jorum.ac.uk/xmlui/themes/Jorum_v2/images/edina_mimas.gif" />
		</p>
</div>

	
</div>

<!--[if lt IE 7 ]>
    <script src="js/dd_belatedpng.js?v=1"></script>
	<script>
	  DD_belatedPNG.fix('.badge_png');
	</script>
<![endif]-->

</body>

</html>
	
