package com.aplaline.pico.actions

import groovy.io.*

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class LogAction extends Action {
	boolean configure(String[] args) {
		def options = opts("pico tags", args, [:])
		if (!options) return false

		if (options.arguments().size() > 0) {
			println "ERROR: Unknown argument(s) ${args}"
			return false
		}
		
		return true
	}

	void execute() {
		Utils.ensureProperFolderStructure()
		new File(".pico/tags").eachFile(FileType.FILES) { tag ->
			print tag.name + " "
			tag.withReader { println it.readLine() }
		}
	}
}
