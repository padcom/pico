package com.aplaline.pico.actions

import java.util.zip.*

import groovy.io.*

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class CommitAction implements Action {
	private String[] args

	void configure(String[] args) {
		this.args = args
	}

	void execute() {
		def tree = new Tree()
		def treeId = ""
	
		new File(".").eachFileRecurse(FileType.FILES) { file ->
			if (file.absolutePath.contains(".pico")) return
	
			println Utils.makeRelative(file.absolutePath)
			def entry = new Entry(path: Utils.makeRelative(file.absolutePath))
	
			def temp = new ByteArrayOutputStream()
			def zip  = new GZIPOutputStream(temp)
			zip.write(file.bytes)
			zip.finish()
			entry.blob = temp.toByteArray()
			entry.id = Utils.sha1(entry.blob)
			println entry.id
			treeId += entry.id
		
			tree.entries.add(entry)
		}
	
		tree.id = Utils.sha1(treeId.bytes)
	
		def commit = new Commit(tree: tree)
		commit.message = args[1]
	
		def head = new File(".pico/HEAD")
	
		if (head.exists()) {
			commit.parentId = head.withReader { it.readLine() }
		}
	
		commit.id = Utils.sha1((commit.parentId + commit.message + tree.id).bytes)
	
		new File(".pico").mkdir()
		new File(".pico/objects").mkdir()
	
		new File(".pico/objects/" + commit.id).withWriter { 
			it.println commit.parentId
			it.println commit.date
			it.println commit.tree.id
			it.println commit.message
		}
	
		new File(".pico/objects/" + commit.tree.id).withWriter { w ->
			commit.tree.entries.each { entry ->
				w.println("${entry.id}=${entry.path}")
			}
		}
	
		commit.tree.entries.each { entry ->
			new File(".pico/objects/" + entry.id).bytes = entry.blob
		}
	
	
		head.withWriter { w -> w.println commit.id }
	}
}