package com.aplaline.pico

import java.security.*

class Utils {
	static ClassLoader parentClassLoader

	static String sha1(byte[] input) {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		new BigInteger(1, md.digest(input)).toString(16).padLeft( 40, '0' ) 
	}

	static String makeRelative(String path) {
		new File(".").toURI().relativize(new File(path).toURI()).getPath();
	}

	static String computeActionClassName(String action) {
		action.replaceAll("(^\\S|(-\\S))") { it[0].toUpperCase() }.split("-").join("") + "Action"
	}

	static Class loadActionClass(String action) {
		GroovyClassLoader loader = new GroovyClassLoader(parentClassLoader);
		loader.parseClass(new File("${System.env['PICO_HOME']}/libexec/com/aplaline/pico/actions/${action}.groovy"))
	}
}
