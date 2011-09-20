package com.aplaline.pico.api

import groovy.io.*

class Tree {
	String id
	List<Entry> entries = new ArrayList<Entry>()

	void read() {
		readMetadata()
		readEntries()
	}

	void readMetadata() {
		new File(".pico/objects/" + id).eachLine { line ->
			def data = line.substring(41).tokenize('|')
			if (data.size() < 2) data = data + ["0"] // Fix for early versions that didn't store file timestamp
			def entry = new Entry(id: line.substring(0, 40), path: data[0], timestamp: data[1] as long)
			entries.add(entry)
		}
	}

	void readEntries() {
		entries.each { entry -> entry.blob = new File(".pico/objects/" + entry.id).bytes }
	}

	void write() {
		writeMetadata()
		writeEntries()
	}

	void writeMetadata() {
		new File(".pico/objects/" + id).withWriter { w ->
			entries.each { entry ->
				w.println("${entry.id}=${entry.path}|${entry.timestamp}")
			}
		}
	}
	
	void writeEntries() {
		entries.each { entry ->
			new File(".pico/objects/" + entry.id).bytes = entry.blob
		}
	}
}
