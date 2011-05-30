
<%@ page import="uk.ac.jorum.search.domain.Creator" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'creator.label', default: 'Creator')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${creatorInstance}">
            <div class="errors">
                <g:renderErrors bean="${creatorInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="value"><g:message code="creator.value.label" default="Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: creatorInstance, field: 'value', 'errors')}">
                                    <g:textArea name="value" cols="40" rows="5" value="${creatorInstance?.value}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="item"><g:message code="creator.item.label" default="Item" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: creatorInstance, field: 'item', 'errors')}">
                                    <g:select name="item.id" from="${uk.ac.jorum.search.domain.Item.list()}" optionKey="id" value="${creatorInstance?.item?.id}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
