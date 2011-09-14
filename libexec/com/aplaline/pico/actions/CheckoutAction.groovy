package com.aplaline.pico.actions

import java.util.zip.*

import groovy.io.*

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class CheckoutAction extends Action {
	private String revision

	private String findRevision(String rev) {
		def commits = []
		new File(".pico/objects").eachFileMatch(FileType.FILES, ~"${rev}.+") { commits << it.name }
		if (commits.size() == 0) {
			println "ERROR: commit ${rev} not found"
			return ""
		} else if (commits.size() > 1) {
			println "ERROR: commit ${rev} is ambigious"
			return ""
		} else {
			return commits[0]
		}
	}

	boolean configure(String[] args) {
		def options = opts("pico checkout [revision]", args, [:])
		if (!options) return false

		def arguments = options.arguments()
		if (arguments.size() > 0) {
			revision = findRevision(arguments[0])
			if (!revision) return false
		} else {
			new File(".pico/HEAD").withReader { revision = it.readLine() }
		}
		
		return true
	}

	void execute() {
		def ant = new AntBuilder()
	
		def commit = new Commit(id: revision)

		new File(".pico/objects/" + commit.id).withReader {
			commit.parentId = it.readLine()
			commit.date = Long.parseLong(it.readLine())
			commit.tree = new Tree()
			commit.tree.id = it.readLine()
		}
	
		new File(".pico/objects/" + commit.tree.id).eachLine { line ->		
			def entry = new Entry(id: line.substring(0, 40), path: ".pico-test/" + line.substring(41))
			entry.blob = new File(".pico/objects/" + entry.id).bytes
			commit.tree.entries.add(entry)
		}
	
		new File(".pico-test/").mkdir()
	
		commit.tree.entries.each { entry ->
			println entry.path
			def output = new File(entry.path)
			ant.mkdir(dir: output.parent)
			output.bytes = new GZIPInputStream(new ByteArrayInputStream(entry.blob)).bytes
		}
	}
}
