
package com.thedemgel.regions;

import com.thedemgel.regions.command.PlayerCommands;
import com.thedemgel.regions.command.RazCommand;
import com.thedemgel.regions.data.PluginFeatures;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.parser.EventParser;
import com.thedemgel.regions.util.TicksPerSecondMonitor;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.volumes.VolumeBox;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.spout.api.command.annotated.AnnotatedCommandExecutorFactory;
import org.spout.api.plugin.Plugin;
import org.spout.api.plugin.PluginLogger;
import org.spout.api.scheduler.TaskPriority;


public class Regions extends Plugin {
	//private Engine engine;
	private static Regions instance;
	private TicksPerSecondMonitor tpsMonitor;
	//private EventParser eventParser;
	private RegisterEvents eventRegister;
	private FeatureRegister featureRegister;
	
	private Map<String, Class<? extends Volume>> volumes = new HashMap<>();
	private Map<String, String> volumeDesc = new HashMap<>();
	
	//private Map<Plugin, PluginFeatures> features = new ConcurrentHashMap<>();

	@Override
	public void onLoad() {
		setInstance(this);
		((PluginLogger) getLogger()).setTag(ChatStyle.RESET + "[" + ChatStyle.GOLD + "Regions" + ChatStyle.RESET + "] ");
		engine = getEngine();
		
		getLogger().info("loaded");
	}
	
	@Override
	public void onEnable() {
		//eventParser = new EventParser();
		setEventRegister(new RegisterEvents(this));
		
		//Commands
		AnnotatedCommandExecutorFactory.create(new RazCommand(this));
		AnnotatedCommandExecutorFactory.create(new PlayerCommands(this), engine.getCommandManager().getCommand("region"));

		engine.getEventManager().registerEvents(new PlayerListener(this), this);
		
		tpsMonitor = new TicksPerSecondMonitor();
		getEngine().getScheduler().scheduleSyncRepeatingTask(this, tpsMonitor, 0, 50, TaskPriority.CRITICAL);

		registerVolume("box", "Basic BoundingBox", VolumeBox.class);
		
		getLogger().log(Level.INFO, "v" + getDescription().getVersion() + " enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "v" + getDescription().getVersion() + " disabled.");
	}
	
	private static void setInstance(Regions instance) {
		Regions.instance = instance;
	}
	
	public static Regions getInstance() {
		return instance;
	}
	
	public void registerVolume(String name, String description, Class<? extends Volume> volume) {
		volumes.put(name, volume);
		volumeDesc.put(name, description);
	}
	
	public Map<String, String> getTypeDesc() {
		return volumeDesc;
	}
	
	/*public void registerFeature(Plugin plugin, Class<? extends Feature> feature) {
		registerFeature(plugin, feature, eventParser);
	}
	
	public void registerFeature(Plugin plugin, Class<? extends Feature> feature, EventParser parser) {
		if (!features.containsKey(plugin)) {
			features.put(plugin, new PluginFeatures());
		}
		features.get(plugin).getFeatures().put(feature.getSimpleName(), feature);
		
		eventRegister.registerEvents(feature, parser);
	}
	
	public Map<Plugin, PluginFeatures> getFeatures() {
		return features;
	}
	
	public Class<? extends Feature> getFeature(Plugin plugin, String SimpleName) {
		if (features.containsKey(plugin)) {
			return features.get(plugin).getFeatures().get(SimpleName);
		}
		
		return null;
	}*/
	
	public Class<? extends Volume> getVolume(String name) {
		return volumes.get(name);
	}
	
	public TicksPerSecondMonitor getTPDMonitor() {
		return tpsMonitor;
	}

	public RegisterEvents getEventRegister() {
		return eventRegister;
	}

	public void setEventRegister(RegisterEvents eventRegister) {
		this.eventRegister = eventRegister;
	}

	public FeatureRegister getFeatureRegister() {
		return featureRegister;
	}

	public void setFeatureRegister(FeatureRegister featureRegister) {
		this.featureRegister = featureRegister;
	}
}
