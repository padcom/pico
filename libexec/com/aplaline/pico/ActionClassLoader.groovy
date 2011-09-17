package com.aplaline.pico

class ActionClassLoader {
	private static scriptClassLoader

	static initialize(ClassLoader scriptClassLoader) {
		this.scriptClassLoader = scriptClassLoader
	}

	static String computeActionClassName(String command) {
		command.replaceAll("(^\\S|(-\\S))") { it[0].toUpperCase() }.split("-").join("") + "Action"
	}

	static Class loadActionClassForCommand(String command) {
		def actionClassName = computeActionClassName(command)
		GroovyClassLoader loader = new GroovyClassLoader(scriptClassLoader);
		loader.parseClass(new File("${System.env['PICO_HOME']}/libexec/com/aplaline/pico/actions/${actionClassName}.groovy"))
	}
}
