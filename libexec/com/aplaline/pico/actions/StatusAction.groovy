package com.aplaline.pico.actions

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class StatusAction extends Action {
	boolean configure(String[] args) {
		return true
	}

	void execute() {
		def head = new Commit(id: Commit.HEAD())
		head.read()
		head.tree.read()
		head.tree.entries.each { entry ->
			def timestamp = new File(entry.path).lastModified()
			if (timestamp != entry.timestamp)
				println "M ${entry.path}"
		}
	}
}
