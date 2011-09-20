package com.aplaline.pico.api

import java.util.zip.*

import com.aplaline.pico.*

class Entry {
	String id
	String path
	long timestamp
	byte[] blob

	static Entry create(File file) {
		def entry = new Entry(path: Utils.makeRelative(file.absolutePath), timestamp: file.lastModified())
		def temp = new ByteArrayOutputStream()
		def zip  = new GZIPOutputStream(temp)
		zip.write(file.bytes)
		zip.finish()
		entry.blob = temp.toByteArray()
		entry.id = Utils.sha1(entry.blob)
		return entry
	}
}
