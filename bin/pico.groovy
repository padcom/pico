import java.util.zip.*
import java.security.*
import groovy.io.*
import java.text.*

import com.aplaline.pico.*
import com.aplaline.pico.api.*
import com.aplaline.pico.actions.*

if (args.size() > 0 && args[0] == "log") {
	def action = new LogAction()
	action.configure(args)
	action.execute()
}

else if (args.size() > 1 && args[0] == "ci") {
	Action action = new CommitAction()
	action.configure(args)
	action.execute()
}

else if (args.size() > 0 && args[0] == "co") {
	Action action = new CheckoutAction()
	action.configure(args)
	action.execute()
	return
} 

