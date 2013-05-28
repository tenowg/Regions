
package com.thedemgel.regions.data;

import com.thedemgel.regions.feature.Feature;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class PluginFeatures {
	private Map<String, Class<? extends Feature>> features = new ConcurrentHashMap<>();
	
	public Map<String, Class<? extends Feature>> getFeatures() {
		return features;
	}
}
