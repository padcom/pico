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
}
