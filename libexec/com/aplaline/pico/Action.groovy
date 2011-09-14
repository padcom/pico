package com.aplaline.pico

abstract class Action {
	protected opts(usage, args, defs) {
		defs.h = [ longOpt: "help", description: "Show usage information" ]

		def cli = new CliBuilder(usage: usage, header: "Options:")
		defs.each { name, opts ->
			cli."${name}"(opts, opts.description)
		}

		def options = cli.parse(args)

		if (!options) {
			return false
		}

		if (options.h) {
			cli.usage()
			return false
		}

		return options
	}

	abstract boolean configure(String[] args)
	abstract void execute()
}
