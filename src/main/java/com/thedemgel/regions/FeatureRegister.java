/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedemgel.regions;

import com.thedemgel.regions.annotations.FeatureName;
import com.thedemgel.regions.data.PluginFeatures;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.parser.EventParser;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.spout.api.plugin.Plugin;

/**
 * Handle registering features and retrieving features.
 */
public class FeatureRegister {

	//private final Regions plugin;
	private final RegisterEvents eventRegister;
	private EventParser eventParser;
	
	private Map<Plugin, PluginFeatures> features = new ConcurrentHashMap<>();
	
	public FeatureRegister(Regions plugin) {
		//this.plugin = plugin;
		eventParser = new EventParser();
		eventRegister = new RegisterEvents(plugin);
	}
	
	public String parseName(Class<? extends Feature> feature) {
		String name = "";
		
		if (feature.isAnnotationPresent(FeatureName.class)) {
			FeatureName fname = feature.getAnnotation(FeatureName.class);
			name = fname.value();
		}
		
		if (name.equals("")) {
			name = feature.getSimpleName();
		}
		
		return name;
	}
	
	public void registerFeature(Plugin plugin, Class<? extends Feature> feature) {
		registerFeature(plugin, feature, eventParser);
	}
	
	public void registerFeature(Plugin plugin, Class<? extends Feature> feature, EventParser parser) {
		if (!features.containsKey(plugin)) {
			features.put(plugin, new PluginFeatures());
		}
		features.get(plugin).getFeatures().put(parseName(feature), feature);
		
		eventRegister.registerEvents(feature, parser);
	}
	
	public Map<Plugin, PluginFeatures> getFeatures() {
		return features;
	}
	
	public Class<? extends Feature> getFeature(Plugin plugin, Class<? extends Feature> feature) {
		return getFeature(plugin, parseName(feature));
	}
	
	public Class<? extends Feature> getFeature(Plugin plugin, String SimpleName) {
		if (features.containsKey(plugin)) {
			return features.get(plugin).getFeatures().get(SimpleName);
		}
		
		return null;
	}
}
