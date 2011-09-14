package com.aplaline.pico

import org.ini4j.Ini

class Preferences {
	static aliases = [:]

	static loadUserSettings() {
		def userConfigFile = new File(System.getProperty("user.home") + "/.pico-config")

		if (userConfigFile.exists()) {
			def config = new Ini(userConfigFile)
			aliases << config.get("aliases")
		}
	}
}
