package com.aplaline.pico.api

class Commit {
	String id
	String parentId
	String message
	long date = Calendar.instance.timeInMillis
	Tree tree

	void read() {
		new File(".pico/objects/" + id).withReader {
			parentId = it.readLine()
			date = Long.parseLong(it.readLine())
			tree = new Tree()
			tree.id = it.readLine()
			message = it.readLine()
		}
	}

	void write() {
		new File(".pico/objects/" + id).withWriter { 
			it.println parentId
			it.println date
			it.println tree.id
			it.println message
		}
	}

	Calendar getTimestamp() {
		def result = Calendar.instance
		result.timeInMillis = date
		return result
	}

	static String getHEAD() {
		def revision = "null"
		def head = new File(".pico/HEAD")
		if (head.exists()) {
			head.withReader { revision = it.readLine() }
		}
		return revision
	}

	static void setHEAD(String id) {
		def head = new File(".pico/HEAD")
		head.withWriter { w -> w.println id }
	}
}
