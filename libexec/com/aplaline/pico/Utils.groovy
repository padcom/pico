package com.aplaline.pico

import java.security.*

import groovy.io.*

class Utils {
	static ClassLoader parentClassLoader

	static String sha1(byte[] input) {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		new BigInteger(1, md.digest(input)).toString(16).padLeft( 40, '0' ) 
	}

	static String makeRelativePath(String path) {
		new File(".").toURI().relativize(new File(path).toURI()).getPath();
	}

	static void makePicoFolderHidden() {
		Runtime.getRuntime().exec("attrib +H .pico");
	}

	static String findRevision(String rev) {
		def commits = []
		new File(".pico/objects").eachFileMatch(FileType.FILES, ~"${rev}.+") { commits << it.name }
		if (commits.size() == 0) {
			println "ERROR: commit ${rev} not found"
			return ""
		} else if (commits.size() > 1) {
			println "ERROR: commit ${rev} is ambigious"
			return ""
		} else {
			return commits[0]
		}
	}

	static void ensureProperFolderStructure() {
		new File(".pico").mkdir()
		new File(".pico/objects").mkdir()
		new File(".pico/tags").mkdir()
	}
}
