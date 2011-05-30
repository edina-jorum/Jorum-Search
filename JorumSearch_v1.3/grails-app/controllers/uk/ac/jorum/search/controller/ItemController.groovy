package uk.ac.jorum.search.controller

import grails.plugins.springsecurity.Secured
import uk.ac.jorum.search.domain.Item

@Secured(['ROLE_ADMIN'])
class ItemController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
    def index = { redirect(action: "list", params: params) }
    
    
    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [itemInstanceList: Item.list(params), itemInstanceTotal: Item.count()]
    }

    def create = {
        def itemInstance = new Item()
        itemInstance.properties = params
        return [itemInstance: itemInstance]
    }

    def save = {
        //def genre = Genre.findByName(params.genre)
        //if(!genre) 
         // genre = new Genre(name: params.genre)
        //params.genre = genre
        def itemInstance = new Item(params)

        //itemInstance.genre = genre
        if (itemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'item.title', default: 'Item'), itemInstance.id])}"
            redirect(action: "show", id: itemInstance.id)
        }
        else {
            render(view: "create", model: [itemInstance: itemInstance])
        }
    }

    def show = {
        def itemInstance = Item.get(params.id)
        if (!itemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'item.title', default: 'Item'), params.id])}"
            redirect(action: "list")
        }
        else {
            [itemInstance: itemInstance]
        }
    }

    def edit = {    
        def itemInstance = Item.get(params.id)
        if (!itemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'item.title', default: 'Item'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [itemInstance: itemInstance]
        }
    }

    def update = {
        //def genre = Genre.findByName(params.genre)
        //if(!genre) 
         // genre = new Genre(name: params.genre).save(flush:true)
        //params.genre = genre      
        def itemInstance = Item.get(params.id)
        if (itemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (itemInstance.version > version) {
                    
                    itemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'item.title', default: 'Item')] as Object[], "Another user has updated this Item while you were editing")
                    render(view: "edit", model: [itemInstance: itemInstance])
                    return
                }
            }
            itemInstance.properties = params
            if (!itemInstance.hasErrors() && itemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'item.title', default: 'Item'), itemInstance.id])}"
                redirect(action: "show", id: itemInstance.id)
            }
            else {
                render(view: "edit", model: [itemInstance: itemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'item.title', default: 'Item'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def itemInstance = Item.get(params.id)
        if (itemInstance) {
            try {
                itemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'item.title', default: 'Item'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'item.title', default: 'Item'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'item.title', default: 'Item'), params.id])}"
            redirect(action: "list")
        }
    }
    
}
