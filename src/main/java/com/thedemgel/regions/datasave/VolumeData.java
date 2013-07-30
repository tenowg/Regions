/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedemgel.regions.datasave;

import java.util.Map;
import org.spout.cereal.config.ConfigurationException;
import org.spout.cereal.config.ConfigurationHolderConfiguration;
import org.spout.cereal.config.ConfigurationNode;
import org.spout.cereal.config.MapConfiguration;

/**
 *
 * @author tenowg
 */
public class VolumeData extends ConfigurationHolderConfiguration {
	// String = key
	//private ConcurrentMap<String, ConfigurationHolder> keys;
	private final String name;
	private final RegionData parent;
	
	public VolumeData(RegionData parent, String volume) {
		super(new MapConfiguration(parent.getNode("volume", volume).getValues()));
		//this.keys = new ConcurrentHashMap<>();
		this.parent = parent;
		this.name = volume;
	}

	public String getName() {
		return name;
	}

	public RegionData getParent() {
		return parent;
	}
	
	public void setValue(String name, Object value) {
		ConfigurationNode node = this.getParent().getNode("volume", this.getName());
		
		node.getNode(name).setValue(value);
	}
	
	public Object getValue(String name) {
		ConfigurationNode node = this.getParent().getNode("volume", this.getName());
		
		return node.getNode(name).getValue();
	}
	
	@Override
	public void load() throws ConfigurationException {
		this.setConfiguration(new MapConfiguration(this.getParent().getNode("volume", this.getName()).getValues()));
		// Loop to add to map
		/*for (Map.Entry<String, Object> entry : this.getValues().entrySet()) {
			ConfigurationHolder holder = new ConfigurationHolder(entry.getValue(), entry.getKey());
			keys.put(entry.getKey(), holder);
		}*/
		super.load();
	}

	@Override
	public void save() throws ConfigurationException {
		super.save();
		ConfigurationNode node = this.getParent().getNode("volume", this.getName());
		for (Map.Entry<String, Object> entry : this.getValues().entrySet()) {
			node.getNode(entry.getKey()).setValue(entry.getValue());
		}	
	}
}
