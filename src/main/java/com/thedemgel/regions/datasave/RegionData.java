/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedemgel.regions.datasave;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import java.io.File;
import java.util.UUID;
import java.util.logging.Level;
import org.spout.cereal.config.ConfigurationException;
import org.spout.cereal.config.ConfigurationHolderConfiguration;
import org.spout.cereal.config.yaml.YamlConfiguration;

/**
 *
 * @author tenowg
 */
public class RegionData extends YamlConfiguration {
	public VolumeData VOLUME = null;
	
	public RegionData(Region region) {
		super(new File(Regions.getInstance().getDataFolder() + File.separator + "saves", region.getUUID().toString() + ".yml"));
		VOLUME = new VolumeData(this, "volume");
	}
	
	/**
	 * Gets the world configuration of a certain world<br> Creates a new one
	 * if it doesn't exist
	 *
	 * @param worldname of the configuration
	 * @return the World configuration node
	 */
	public final VolumeData get(String volume) {
			if (VOLUME == null) {
				return new VolumeData(this, volume);
			}
			return VOLUME;
	}
	
	@Override
	public void load() {
		try {
			super.load();
			VOLUME.load();
		} catch (ConfigurationException e) {
			Regions.getInstance().getLogger().log(Level.WARNING, "Error loading Feature configuration: ", e);
		}
	}

	@Override
	public void save() {
		try {
			VOLUME.save();
			super.save();
		} catch (ConfigurationException e) {
			Regions.getInstance().getLogger().log(Level.WARNING, "Error saving Feature configuration: ", e);
		}
	}
}
