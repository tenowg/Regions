/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedemgel.regions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.thedemgel.regions.annotations.FeatureName;
import com.thedemgel.regions.data.PluginFeatures;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.parser.EventParser;

import org.spout.api.plugin.Plugin;

/**
 * Handle registering features and retrieving features.
 */
public class FeatureRegister {
	private final RegisterEvents eventRegister;
	private final EventParser eventParser;

	private Map<Plugin, PluginFeatures> features = new ConcurrentHashMap<>();

	public FeatureRegister(final Regions plugin) {
		eventParser = new EventParser();
		eventRegister = new RegisterEvents(plugin);
	}

	public final String parseName(Class<? extends Feature> feature) {
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

	public final void registerFeature(Plugin plugin, Class<? extends Feature> feature) {
		registerFeature(plugin, feature, eventParser);
	}

	public final void registerFeature(Plugin plugin, Class<? extends Feature> feature, EventParser parser) {
		if (!features.containsKey(plugin)) {
			features.put(plugin, new PluginFeatures());
		}
		features.get(plugin).getFeatures().put(parseName(feature), feature);

		eventRegister.registerEvents(feature, parser);
	}

	public final Map<Plugin, PluginFeatures> getFeatures() {
		return features;
	}

	public final Class<? extends Feature> getFeature(Plugin plugin, Class<? extends Feature> feature) {
		return getFeature(plugin, parseName(feature));
	}

	public final Class<? extends Feature> getFeature(Plugin plugin, String simpleName) {
		if (features.containsKey(plugin)) {
			return features.get(plugin).getFeatures().get(simpleName);
		}
		return null;
	}
}
