/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedemgel.regions.datasave;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.data.Region;
import java.io.File;
import java.util.logging.Level;
import org.spout.cereal.config.ConfigurationException;
import org.spout.cereal.config.yaml.YamlConfiguration;

/**
 *
 * @author tenowg
 */
public class RegionData extends YamlConfiguration {
	public VolumeData VOLUME = null;
	private SaveData saveData;
	private Region region;
	
	public RegionData(Region region) {
		super(new File(Regions.getInstance().getDataFolder() + File.separator + "saves", region.getUUID().toString() + ".yml"));
		this.region = region;
		saveData = new SaveData(this);
		VOLUME = new VolumeData(this, "volume");
	}
	
	/**
	 * Gets the volume data needed to reconstruct a volume.
	 *
	 * @param volume of the configuration
	 * @return the Volume configuration node
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
			saveData.save();
			VOLUME.save();
			super.save();
		} catch (ConfigurationException e) {
			Regions.getInstance().getLogger().log(Level.WARNING, "Error saving Feature configuration: ", e);
		}
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
}
