import com.aplaline.pico.*
import com.aplaline.pico.api.*
import com.aplaline.pico.actions.*

Utils.parentClassLoader = getClass().getClassLoader()
Utils.makePicoFolderHidden()

Preferences.loadUserSettings()

if (args.size() > 0) {
	def command = Preferences.aliases[args[0]] ? Preferences.aliases[args[0]] : args[0]

	def actionClass = Utils.loadActionClass(Utils.computeActionClassName(command))
	def action = (Action) actionClass.newInstance()
	if (action.configure(args.tail())) action.execute()
}
