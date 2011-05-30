
<%@ page import="uk.ac.jorum.search.domain.Item" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'item.label', default: 'Item')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'item.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="oai_identifier" title="${message(code: 'item.oai_identifier.label', default: 'Oaiidentifier')}" />
                        
                            <g:sortableColumn property="repository_name" title="${message(code: 'item.repository_name.label', default: 'Repositoryname')}" />
                        
                            <g:sortableColumn property="oai_datestamp" title="${message(code: 'item.oai_datestamp.label', default: 'Oaidatestamp')}" />
                        
                            <g:sortableColumn property="meta_title" title="${message(code: 'item.meta_title.label', default: 'Metatitle')}" />
                        
                            <g:sortableColumn property="meta_identifier" title="${message(code: 'item.meta_identifier.label', default: 'Metaidentifier')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${itemInstanceList}" status="i" var="itemInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${itemInstance.id}">${fieldValue(bean: itemInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: itemInstance, field: "oai_identifier")}</td>
                        
                            <td>${fieldValue(bean: itemInstance, field: "repository_name")}</td>
                        
                            <td>${fieldValue(bean: itemInstance, field: "oai_datestamp")}</td>
                        
                            <td>${fieldValue(bean: itemInstance, field: "meta_title")}</td>
                        
                            <td>${fieldValue(bean: itemInstance, field: "meta_identifier")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${itemInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
