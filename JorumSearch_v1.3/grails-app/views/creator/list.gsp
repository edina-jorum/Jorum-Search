
<%@ page import="uk.ac.jorum.search.domain.Creator" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'creator.label', default: 'Creator')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'creator.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="value" title="${message(code: 'creator.value.label', default: 'Value')}" />
                        
                            <th><g:message code="creator.item.label" default="Item" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${creatorInstanceList}" status="i" var="creatorInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${creatorInstance.id}">${fieldValue(bean: creatorInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: creatorInstance, field: "value")}</td>
                        
                            <td>${fieldValue(bean: creatorInstance, field: "item")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${creatorInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
