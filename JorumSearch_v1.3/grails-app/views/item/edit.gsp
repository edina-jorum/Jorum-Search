
<%@ page import="uk.ac.jorum.search.domain.Item" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'item.label', default: 'Item')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${itemInstance}">
            <div class="errors">
                <g:renderErrors bean="${itemInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${itemInstance?.id}" />
                <g:hiddenField name="version" value="${itemInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="oai_identifier"><g:message code="item.oai_identifier.label" default="Oaiidentifier" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'oai_identifier', 'errors')}">
                                    <g:textField name="oai_identifier" value="${itemInstance?.oai_identifier}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="repository_name"><g:message code="item.repository_name.label" default="Repositoryname" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'repository_name', 'errors')}">
                                    <g:textField name="repository_name" value="${itemInstance?.repository_name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="oai_datestamp"><g:message code="item.oai_datestamp.label" default="Oaidatestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'oai_datestamp', 'errors')}">
                                    <g:textField name="oai_datestamp" value="${itemInstance?.oai_datestamp}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_title"><g:message code="item.meta_title.label" default="Metatitle" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_title', 'errors')}">
                                    <g:textArea name="meta_title" cols="40" rows="5" value="${itemInstance?.meta_title}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_identifier"><g:message code="item.meta_identifier.label" default="Metaidentifier" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_identifier', 'errors')}">
                                    <g:textField name="meta_identifier" value="${itemInstance?.meta_identifier}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_identifier_uri"><g:message code="item.meta_identifier_uri.label" default="Metaidentifieruri" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_identifier_uri', 'errors')}">
                                    <g:textArea name="meta_identifier_uri" cols="40" rows="5" value="${itemInstance?.meta_identifier_uri}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_description"><g:message code="item.meta_description.label" default="Metadescription" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_description', 'errors')}">
                                    <g:textArea name="meta_description" cols="40" rows="5" value="${itemInstance?.meta_description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_language"><g:message code="item.meta_language.label" default="Metalanguage" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_language', 'errors')}">
                                    <g:textField name="meta_language" value="${itemInstance?.meta_language}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_date_available"><g:message code="item.meta_date_available.label" default="Metadateavailable" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_date_available', 'errors')}">
                                    <g:textField name="meta_date_available" value="${itemInstance?.meta_date_available}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_date_issued"><g:message code="item.meta_date_issued.label" default="Metadateissued" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_date_issued', 'errors')}">
                                    <g:textField name="meta_date_issued" value="${itemInstance?.meta_date_issued}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_date_created"><g:message code="item.meta_date_created.label" default="Metadatecreated" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_date_created', 'errors')}">
                                    <g:textField name="meta_date_created" value="${itemInstance?.meta_date_created}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_format"><g:message code="item.meta_format.label" default="Metaformat" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_format', 'errors')}">
                                    <g:textField name="meta_format" value="${itemInstance?.meta_format}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_rights"><g:message code="item.meta_rights.label" default="Metarights" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_rights', 'errors')}">
                                    <g:textField name="meta_rights" value="${itemInstance?.meta_rights}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="meta_rights_uri"><g:message code="item.meta_rights_uri.label" default="Metarightsuri" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'meta_rights_uri', 'errors')}">
                                    <g:textField name="meta_rights_uri" value="${itemInstance?.meta_rights_uri}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="creators"><g:message code="item.creators.label" default="Creators" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'creators', 'errors')}">
                                    
<ul>
<g:each in="${itemInstance?.creators?}" var="c">
    <li><g:link controller="creator" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="creator" action="create" params="['item.id': itemInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'creator.label', default: 'Creator')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="publishers"><g:message code="item.publishers.label" default="Publishers" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'publishers', 'errors')}">
                                    
<ul>
<g:each in="${itemInstance?.publishers?}" var="p">
    <li><g:link controller="publisher" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="publisher" action="create" params="['item.id': itemInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'publisher.label', default: 'Publisher')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dateCreated"><g:message code="item.dateCreated.label" default="Date Created" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'dateCreated', 'errors')}">
                                    <g:datePicker name="dateCreated" precision="day" value="${itemInstance?.dateCreated}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="lastUpdated"><g:message code="item.lastUpdated.label" default="Last Updated" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'lastUpdated', 'errors')}">
                                    <g:datePicker name="lastUpdated" precision="day" value="${itemInstance?.lastUpdated}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="subjects"><g:message code="item.subjects.label" default="Subjects" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemInstance, field: 'subjects', 'errors')}">
                                    
<ul>
<g:each in="${itemInstance?.subjects?}" var="s">
    <li><g:link controller="subject" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="subject" action="create" params="['item.id': itemInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'subject.label', default: 'Subject')])}</g:link>

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
