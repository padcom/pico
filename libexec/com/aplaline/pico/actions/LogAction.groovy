package com.aplaline.pico.actions

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class LogAction extends Action {
	boolean configure(String[] args) {
		def options = opts("pico log", args, [:])
		if (!options) return false

		if (options.arguments().size() > 0) {
			println "ERROR: Unknown argument(s) ${args}"
			return false
		}
		
		return true
	}

	void execute() {
		def commit = new Commit(id: Commit.HEAD)
		while (commit.id != 'null') {
			commit.read()
			println "commit ${commit.id}"
			println "Author: Unknown <unknown@host.com>"
			println "Date ${commit.timestamp.format('yyyy-MM-dd HH:mm:ss')}"
			println "\n${commit.message}\n"
			commit.id = commit.parentId
		}
	}
}
