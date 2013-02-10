
package com.thedemgel.regions.data;

import java.io.Serializable;
import org.spout.api.util.list.concurrent.ConcurrentList;


public class FeaturePlugin implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String plugin;
	private ConcurrentList<String> features = new ConcurrentList<String>();
	
	public FeaturePlugin(String plugin) {
		this.plugin = plugin;
	}
	
	public String getPlugin() {
		return plugin;
	}
	
	public ConcurrentList<String> getFeatures() {
		return features;
	}
}
