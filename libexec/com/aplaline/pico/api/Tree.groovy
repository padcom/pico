package com.aplaline.pico.api

import groovy.io.*

class Tree {
	String id
	List<Entry> entries = new ArrayList<Entry>()

	void read() {
		readMetadata()
		entries.each { entry -> entry.blob = new File(".pico/objects/" + entry.id).bytes }
	}

	void readMetadata() {
		new File(".pico/objects/" + id).eachLine { line ->
			def data = line.substring(41).tokenize('|')
			def entry = new Entry(id: line.substring(0, 40), path: data[0], timestamp: data[1] as long)
			entries.add(entry)
		}
	}
}
