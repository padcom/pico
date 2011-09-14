package com.aplaline.pico.actions

import java.util.zip.*

import groovy.io.*

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class CheckoutAction extends Action {
	private String[] args

	boolean configure(String[] args) {
		this.args = args
		return true
	}

	void execute() {
		def ant = new AntBuilder()
	
		def commit = new Commit()
		if (args.size() == 1)
			new File(".pico/HEAD").withReader { commit.id = it.readLine() }
		else {
			def commits = []
			new File(".pico/objects").eachFileMatch(FileType.FILES, ~"${args[1]}.+") { commits << it.name }
			if (commits.size() == 0) {
				println "Error: commit ${args[1]} not found"
				return
			} else if (commits.size() > 1) {
				println "Error: commit ${args[1]} is ambigious"
				return
			} else {
				commit.id = commits[0]
			}
		}

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
