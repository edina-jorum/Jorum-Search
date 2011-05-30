package uk.ac.jorum.search.controller

import grails.plugins.springsecurity.Secured
import uk.ac.jorum.search.domain.Creator

@Secured(['ROLE_ADMIN'])
class CreatorController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [creatorInstanceList: Creator.list(params), creatorInstanceTotal: Creator.count()]
    }

    def create = {
        def creatorInstance = new Creator()
        creatorInstance.properties = params
        return [creatorInstance: creatorInstance]
    }

    def save = {
        def creatorInstance = new Creator(params)
        if (creatorInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'creator.label', default: 'Creator'), creatorInstance.id])}"
            redirect(action: "show", id: creatorInstance.id)
        }
        else {
            render(view: "create", model: [creatorInstance: creatorInstance])
        }
    }

    def show = {
        def creatorInstance = Creator.get(params.id)
        if (!creatorInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creator.label', default: 'Creator'), params.id])}"
            redirect(action: "list")
        }
        else {
            [creatorInstance: creatorInstance]
        }
    }

    def edit = {
        def creatorInstance = Creator.get(params.id)
        if (!creatorInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creator.label', default: 'Creator'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [creatorInstance: creatorInstance]
        }
    }

    def update = {
        def creatorInstance = Creator.get(params.id)
        if (creatorInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (creatorInstance.version > version) {
                    
                    creatorInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'creator.label', default: 'Creator')] as Object[], "Another user has updated this Creator while you were editing")
                    render(view: "edit", model: [creatorInstance: creatorInstance])
                    return
                }
            }
            creatorInstance.properties = params
            if (!creatorInstance.hasErrors() && creatorInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'creator.label', default: 'Creator'), creatorInstance.id])}"
                redirect(action: "show", id: creatorInstance.id)
            }
            else {
                render(view: "edit", model: [creatorInstance: creatorInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creator.label', default: 'Creator'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def creatorInstance = Creator.get(params.id)
        if (creatorInstance) {
            try {
                creatorInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'creator.label', default: 'Creator'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'creator.label', default: 'Creator'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creator.label', default: 'Creator'), params.id])}"
            redirect(action: "list")
        }
    }
}
