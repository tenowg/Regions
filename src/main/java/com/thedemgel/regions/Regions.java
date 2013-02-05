
package com.thedemgel.regions;

import com.thedemgel.regions.command.PlayerCommands;
import java.util.logging.Level;
import org.spout.api.Engine;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.RootCommand;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.PluginLogger;


public class Regions extends CommonPlugin {
	private Engine engine;
	private static Regions instance;

	@Override
	public void onLoad() {
		setInstance(this);
		((PluginLogger) getLogger()).setTag(new ChatArguments(ChatStyle.RESET, "[", ChatStyle.GOLD, "Regions", ChatStyle.RESET, "] "));
		engine = getEngine();
		getLogger().info("loaded");
	}
	
	@Override
	public void onEnable() {
		//Commands
		final CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());
		final RootCommand root = engine.getRootCommand();
		root.addSubCommands(this, PlayerCommands.class, commandRegFactory);
		//root.addSubCommands(this, AdminCommands.class, commandRegFactory);

		engine.getEventManager().registerEvents(new PlayerListener(this), this);

		getLogger().log(Level.INFO, "v{0} enabled.", getDescription().getVersion());
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "v{0} disabled.", getDescription().getVersion());
	}
	
	private static void setInstance(Regions instance) {
		Regions.instance = instance;
	}
	
	public static Regions getInstance() {
		return instance;
	}
}
