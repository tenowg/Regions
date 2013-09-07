
package com.thedemgel.regions;

import com.thedemgel.regions.command.PlayerCommands;
import com.thedemgel.regions.command.RazCommand;
import com.thedemgel.regions.util.TicksPerSecondMonitor;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.volumes.VolumeBox;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.spout.api.command.annotated.AnnotatedCommandExecutorFactory;
import org.spout.api.plugin.Plugin;
import org.spout.api.plugin.PluginLogger;
import org.spout.api.scheduler.TaskPriority;


public class Regions extends Plugin {
	/**
	 * Static instance of Regions plugin.
	 */
	private static Regions instance;
	/**
	 * General TPS monitor, used for onTick and other system critical events.
	 */
	private TicksPerSecondMonitor tpsMonitor;
	//private RegisterEvents eventRegister;
	/**
	 * Base class used to Register a feature, will look for all Events and register them as well.
	 */
	private FeatureRegister featureRegister;

	private Map<String, Class<? extends Volume>> volumes = new HashMap<>();
	private Map<String, String> volumeDesc = new HashMap<>();

	public static final long TPS_INIT_DELAY = 0L;
	public static final long TPS_REPEAT_DELAY = 50L;

	@Override
	public final void onLoad() {
		setInstance(this);
		((PluginLogger) getLogger()).setTag(ChatStyle.RESET + "[" + ChatStyle.GOLD + "Regions" + ChatStyle.RESET + "] ");
		engine = getEngine();

		getLogger().info("loaded");
	}

	@Override
	public final void onEnable() {
		//setEventRegister(new RegisterEvents(this));

		//Commands
		AnnotatedCommandExecutorFactory.create(new RazCommand(this));
		AnnotatedCommandExecutorFactory.create(new PlayerCommands(this), engine.getCommandManager().getCommand("region"));

		engine.getEventManager().registerEvents(new PlayerListener(this), this);

		tpsMonitor = new TicksPerSecondMonitor();
		getEngine().getScheduler().scheduleSyncRepeatingTask(this, tpsMonitor, Regions.TPS_INIT_DELAY, Regions.TPS_REPEAT_DELAY, TaskPriority.CRITICAL);

		registerVolume("box", "Basic BoundingBox", VolumeBox.class);

		getLogger().log(Level.INFO, "v" + getDescription().getVersion() + " enabled.");
	}

	@Override
	public final void onDisable() {
		getLogger().log(Level.INFO, "v" + getDescription().getVersion() + " disabled.");
	}

	private static void setInstance(Regions plugin) {
		Regions.instance = plugin;
	}

	public static Regions getInstance() {
		return instance;
	}

	/**
	 * Will be used to manually register a Volume type. Will eventually be replaced by automatic class/annotation
	 * using reflections.
	 * @param name String name of the volume
	 * @param description String description of the Volume
	 * @param volume Class of the registering volume.
	 */
	public final void registerVolume(String name, String description, Class<? extends Volume> volume) {
		volumes.put(name, volume);
		volumeDesc.put(name, description);
	}

	public final Map<String, String> getTypeDesc() {
		return volumeDesc;
	}

	public final Class<? extends Volume> getVolume(String name) {
		return volumes.get(name);
	}

	public final TicksPerSecondMonitor getTPDMonitor() {
		return tpsMonitor;
	}

	/*public final RegisterEvents getEventRegister() {
		return eventRegister;
	}

	public final void setEventRegister(RegisterEvents register) {
		this.eventRegister = register;
	}*/

	public final FeatureRegister getFeatureRegister() {
		return featureRegister;
	}

	public final void setFeatureRegister(FeatureRegister register) {
		this.featureRegister = register;
	}
}
