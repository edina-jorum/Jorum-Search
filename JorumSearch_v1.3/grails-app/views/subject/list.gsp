
<%@ page import="uk.ac.jorum.search.domain.Subject" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'subject.label', default: 'Subject')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'subject.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="value" title="${message(code: 'subject.value.label', default: 'Value')}" />
                        
                            <th><g:message code="subject.item.label" default="Item" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${subjectInstanceList}" status="i" var="subjectInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${subjectInstance.id}">${fieldValue(bean: subjectInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: subjectInstance, field: "value")}</td>
                        
                            <td>${fieldValue(bean: subjectInstance, field: "item")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${subjectInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
