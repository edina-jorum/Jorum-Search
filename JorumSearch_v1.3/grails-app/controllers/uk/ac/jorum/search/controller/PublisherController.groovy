package uk.ac.jorum.search.controller

import grails.plugins.springsecurity.Secured
import uk.ac.jorum.search.domain.Publisher

@Secured(['ROLE_ADMIN'])
class PublisherController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [publisherInstanceList: Publisher.list(params), publisherInstanceTotal: Publisher.count()]
    }

    def create = {
        def publisherInstance = new Publisher()
        publisherInstance.properties = params
        return [publisherInstance: publisherInstance]
    }

    def save = {
        def publisherInstance = new Publisher(params)
        if (publisherInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'publisher.label', default: 'Publisher'), publisherInstance.id])}"
            redirect(action: "show", id: publisherInstance.id)
        }
        else {
            render(view: "create", model: [publisherInstance: publisherInstance])
        }
    }

    def show = {
        def publisherInstance = Publisher.get(params.id)
        if (!publisherInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'publisher.label', default: 'Publisher'), params.id])}"
            redirect(action: "list")
        }
        else {
            [publisherInstance: publisherInstance]
        }
    }

    def edit = {
        def publisherInstance = Publisher.get(params.id)
        if (!publisherInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'publisher.label', default: 'Publisher'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [publisherInstance: publisherInstance]
        }
    }

    def update = {
        def publisherInstance = Publisher.get(params.id)
        if (publisherInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (publisherInstance.version > version) {
                    
                    publisherInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'publisher.label', default: 'Publisher')] as Object[], "Another user has updated this Publisher while you were editing")
                    render(view: "edit", model: [publisherInstance: publisherInstance])
                    return
                }
            }
            publisherInstance.properties = params
            if (!publisherInstance.hasErrors() && publisherInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'publisher.label', default: 'Publisher'), publisherInstance.id])}"
                redirect(action: "show", id: publisherInstance.id)
            }
            else {
                render(view: "edit", model: [publisherInstance: publisherInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'publisher.label', default: 'Publisher'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def publisherInstance = Publisher.get(params.id)
        if (publisherInstance) {
            try {
                publisherInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'publisher.label', default: 'Publisher'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'publisher.label', default: 'Publisher'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'publisher.label', default: 'Publisher'), params.id])}"
            redirect(action: "list")
        }
    }
}
