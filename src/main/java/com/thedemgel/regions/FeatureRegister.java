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
	/**
	 * Default RegisterEvents used by all plugins.
	 */
	private final RegisterEvents eventRegister;
	/**
	 * Default EventParser
	 */
	private final EventParser eventParser;

	/**
	 * Map containing all the registered features for later lookup.
	 */
	private Map<Plugin, PluginFeatures> features;

	/**
	 * Basic constructor for FeatureRegister.
	 * @param plugin Regions plugin (possibly work on using different plugins)
	 */
	public FeatureRegister(final Regions plugin) {
		this.features = new ConcurrentHashMap<>();
		eventParser = new EventParser();
		eventRegister = new RegisterEvents(plugin);
	}

	/**
	 * Parse a unique name from a Feature by means of the FeatureName annotation.
	 * @param feature Feature that contains a FeatureName class annotation.
	 * @return
	 */
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

	/**
	 * Manual registration of a Feature.
	 * @param plugin Plugin that is registering a Feature.
	 * @param feature
	 */
	public final void registerFeature(Plugin plugin, Class<? extends Feature> feature) {
		registerFeature(plugin, feature, eventParser);
	}

	/**
	 * Manually register a Feature with Regions.
	 * @param plugin Plugin that is registering a Feature
	 * @param feature Class of the Feature being registered
	 * @param parser The Event parser (if not using default)
	 */
	public final void registerFeature(Plugin plugin, Class<? extends Feature> feature, EventParser parser) {
		if (!features.containsKey(plugin)) {
			features.put(plugin, new PluginFeatures());
		}
		features.get(plugin).getFeatures().put(parseName(feature), feature);

		eventRegister.registerEvents(feature, parser);
	}

	/**
	 * Retrieves the full list of registered Features.
	 * @return A map containing all registered Features.
	 */
	public final Map<Plugin, PluginFeatures> getFeatures() {
		return features;
	}

	/**
	 * Search for a feature (by feature class) registered by a plugin.
	 * @param plugin Plugin that registered the feature.
	 * @param feature The class of the feature being searched for.
	 * @return
	 */
	public final Class<? extends Feature> getFeature(Plugin plugin, Class<? extends Feature> feature) {
		return getFeature(plugin, parseName(feature));
	}

	/**
	 * Search for a feature (by feature SimpleName) registered by a plugin.
	 * @param plugin Plugin that registered the feature.
	 * @param simpleName SimpleName of the class registered.
	 * @return
	 */
	public final Class<? extends Feature> getFeature(Plugin plugin, String simpleName) {
		if (features.containsKey(plugin)) {
			return features.get(plugin).getFeatures().get(simpleName);
		}
		return null;
	}
}
