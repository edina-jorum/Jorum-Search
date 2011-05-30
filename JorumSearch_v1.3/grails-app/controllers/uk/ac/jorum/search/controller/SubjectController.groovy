package uk.ac.jorum.search.controller

import grails.plugins.springsecurity.Secured
import uk.ac.jorum.search.domain.Subject

@Secured(['ROLE_ADMIN'])
class SubjectController {

	
    def index = { redirect(action: "list", params: params) }
    
    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [subjectInstanceList: Subject.list(params), subjectInstanceTotal: Subject.count()]
    }
    
    def create = {
        def subjectInstance = new Subject()
        subjectInstance.properties = params
        return [subjectInstance: subjectInstance]
    }

    def save = {
       
        def subjectInstance = new Subject(params)

       
        if (subjectInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'subject.label', default: 'Subject'), subjectInstance.id])}"
            redirect(action: "show", id: subjectInstance.id)
        }
        else {
            render(view: "create", model: [subjectInstance: subjectInstance])
        }
    }

    def show = {
        def subjectInstance = Subject.get(params.id)
        if (!subjectInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'subject.label', default: 'Subject'), params.id])}"
            redirect(action: "list")
        }
        else {
            [subjectInstance: subjectInstance]
        }
    }

    def edit = {    
        def subjectInstance = Subject.get(params.id)
        if (!subjectInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'subject.label', default: 'Subject'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [subjectInstance: subjectInstance]
        }
    }

    def update = {
       
        def subjectInstance = Subject.get(params.id)
        if (subjectInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (subjectInstance.version > version) {
                    
                    subjectInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'subject.label', default: 'Subject')] as Object[], "Another user has updated this Subject while you were editing")
                    render(view: "edit", model: [subjectInstance: subjectInstance])
                    return
                }
            }
            subjectInstance.properties = params
            if (!subjectInstance.hasErrors() && subjectInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'subject.label', default: 'Subject'), subjectInstance.id])}"
                redirect(action: "show", id: subjectInstance.id)
            }
            else {
                render(view: "edit", model: [subjectInstance: subjectInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'subject.label', default: 'Subject'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def subjectInstance = Subject.get(params.id)
        if (subjectInstance) {
            try {
                subjectInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'subject.label', default: 'Subject'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'subject.label', default: 'Subject'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'subject.label', default: 'Subject'), params.id])}"
            redirect(action: "list")
        }
    }
}
