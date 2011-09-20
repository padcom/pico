package com.aplaline.pico.actions

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class TagAction extends Action {
	String tagName
	String revision

	boolean configure(String[] args) {
		def options = opts("pico tag tag-name [revision]", args, [:])
		if (!options) return false

		if (options.arguments().size() < 1) {
			println "ERROR: Need a tag name"
			return false
		} else {
			tagName = options.arguments()[0]
		}

		if (options.arguments().size() < 2) {
        	revision = Commit.HEAD
        } else {
        	revision = Utils.findRevision(options.arguments()[1])
			if (!revision) return false
        }
		
		return true
	}

	void execute() {
		Utils.ensureProperFolderStructure()
		def tag = new File(".pico/tags/" + tagName)
		if (tag.exists()) {
			println "ERROR: tag with the name ${tagName} already exists"
		} else {
			new File(".pico/tags/" + tagName).withWriter { it.println revision }
		}
	}
}
