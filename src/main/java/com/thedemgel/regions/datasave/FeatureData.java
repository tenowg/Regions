/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedemgel.regions.datasave;

import org.spout.cereal.config.ConfigurationHolderConfiguration;
import org.spout.cereal.config.MapConfiguration;

/**
 *
 * @author tenowg
 */
public class FeatureData extends ConfigurationHolderConfiguration {
	private final String name;
	private final RegionData parent;
	
	public FeatureData(RegionData parent, String name) {
		super(new MapConfiguration(parent.getNode("feature", name).getValues()));
		this.parent = parent;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public RegionData getParent() {
		return parent;
	}
}
