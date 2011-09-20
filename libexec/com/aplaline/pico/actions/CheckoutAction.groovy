package com.aplaline.pico.actions

import java.util.zip.*

import groovy.io.*

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class CheckoutAction extends Action {
	private String revision

	boolean configure(String[] args) {
		def options = opts("pico checkout [revision]", args, [:])
		if (!options) return false

		def arguments = options.arguments()
		if (arguments.size() > 0) {
			revision = Utils.findRevision(arguments[0])
			if (!revision) return false
		} else {
			revision = Commit.HEAD
		}
		
		return true
	}

	void execute() {
		def commit = new Commit(id: revision)
		commit.read()
		commit.tree.read()
	
		new File(".pico-test/").mkdirs()
	
		commit.tree.entries.each { entry ->
			println entry.path
			def output = new File(".pico-test/" + entry.path)
			new File(output.parent).mkdirs()
			output.bytes = new GZIPInputStream(new ByteArrayInputStream(entry.blob)).bytes
		}
	}
}
