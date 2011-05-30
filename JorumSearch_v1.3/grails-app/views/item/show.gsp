
<%@ page import="uk.ac.jorum.search.domain.Item" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'item.label', default: 'Item')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.oai_identifier.label" default="Oaiidentifier" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "oai_identifier")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.repository_name.label" default="Repositoryname" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "repository_name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.oai_datestamp.label" default="Oaidatestamp" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "oai_datestamp")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_title.label" default="Metatitle" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_title")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_identifier.label" default="Metaidentifier" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_identifier")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_identifier_uri.label" default="Metaidentifieruri" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_identifier_uri")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_description.label" default="Metadescription" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_description")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_language.label" default="Metalanguage" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_language")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_date_available.label" default="Metadateavailable" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_date_available")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_date_issued.label" default="Metadateissued" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_date_issued")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_date_created.label" default="Metadatecreated" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_date_created")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_format.label" default="Metaformat" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_format")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_rights.label" default="Metarights" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_rights")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.meta_rights_uri.label" default="Metarightsuri" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: itemInstance, field: "meta_rights_uri")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.creators.label" default="Creators" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${itemInstance.creators}" var="c">
                                    <li><g:link controller="creator" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.publishers.label" default="Publishers" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${itemInstance.publishers}" var="p">
                                    <li><g:link controller="publisher" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.dateCreated.label" default="Date Created" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${itemInstance?.dateCreated}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.lastUpdated.label" default="Last Updated" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${itemInstance?.lastUpdated}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="item.subjects.label" default="Subjects" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${itemInstance.subjects}" var="s">
                                    <li><g:link controller="subject" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${itemInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
