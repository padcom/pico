package com.aplaline.pico.actions

import groovy.io.*

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class CommitAction extends Action {
	private String message

	boolean configure(String[] args) {
		def options = opts("pico commit -m message", args, [
			m: [ longOpt: "message", args: 1, argName: "message", description: "Commit message" ],
		])

		if (!options) return

		this.message = options.m
		if (!message) {
			println "ERROR: No message specified"
			return false
		}

		return true
	}

	void execute() {
	    Utils.ensureProperFolderStructure()

		def tree = new Tree()
		def treeId = ""
	
		new File(".").eachFileRecurse(FileType.FILES) { file ->
			if (file.absolutePath.contains(".pico")) return
	
	        def entry = Entry.create(file)
			treeId += entry.id
			println entry.id + " " + entry.path
		
			tree.entries.add(entry)
		}
	
		tree.id = Utils.sha1(treeId.bytes)
	
		def commit = new Commit(tree: tree, message: message)
	
		if (Commit.HEAD != 'null') {
			commit.parentId = Commit.HEAD
		}
	
		commit.id = Utils.sha1((commit.parentId + commit.message + tree.id).bytes)
	
	    commit.write()
	    commit.tree.write()
	
	    Commit.HEAD = commit.id
	}
}
