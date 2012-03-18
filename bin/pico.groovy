import com.aplaline.pico.*
import com.aplaline.pico.api.*
import com.aplaline.pico.actions.*

ActionClassLoader.initialize(getClass().getClassLoader())
Utils.makePicoFolderHidden()

Preferences.loadUserSettings()

if (args.size() > 0) {
	def command = Preferences.aliases[args[0]] ? Preferences.aliases[args[0]] : args[0]

	def actionClass = ActionClassLoader.loadActionClassForCommand(command)
	def action = (Action) actionClass.newInstance()
	if (action.configure(args.tail())) action.execute()
}
