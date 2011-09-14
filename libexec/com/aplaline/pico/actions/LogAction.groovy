package com.aplaline.pico.actions

import com.aplaline.pico.*
import com.aplaline.pico.api.*

class LogAction implements Action {
	void configure(String[] args) {
	}

	void execute() {
		def head = new File(".pico/HEAD").readLines()[0]
		def commit = new File(".pico/objects/" + head)
		while (commit.exists()) {
			commit.withReader {
				def parent = it.readLine()
				println "id       : " + head
				println "parent   : " + parent
				def timestamp = Calendar.instance
				timestamp.timeInMillis = Long.parseLong(it.readLine().trim())
				println "timestamp: " + timestamp.format("yyyy-MM-dd HH:mm:ss")
				println "tree	 : " + it.readLine()
				println "comment  : " + it.readLines().join("\n") + "\n"
				
				commit = new File(".pico/objects/" + parent)
				head = parent
			}
		}
	}
}
