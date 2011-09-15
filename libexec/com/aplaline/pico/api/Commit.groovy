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
		}
	}

	static String HEAD() {
		def revision = ""
		new File(".pico/HEAD").withReader { revision = it.readLine() }
		return revision
	}
}
