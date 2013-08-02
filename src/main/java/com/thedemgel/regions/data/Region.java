package com.thedemgel.regions.data;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.datasave.RegionData;
import com.thedemgel.regions.exception.FeatureNotFoundException;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.feature.FeatureHolder;
import com.thedemgel.regions.util.PointRepresenter;
import com.thedemgel.regions.util.RegionYamlConstructor;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.volumes.VolumeBox;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import org.spout.api.Spout;
import org.spout.api.geo.World;
import org.spout.api.plugin.Plugin;
import org.spout.api.util.list.concurrent.ConcurrentList;
import org.yaml.snakeyaml.Yaml;

public class Region implements Serializable {

	private final static long serialVersionUID = 32L;
	transient private Volume volume;
	private UUID ident = null;
	private String name;
	private String world;
	private ConcurrentList<PointMap> pointCache = new ConcurrentList<>();
	private FeatureHolder holder = new FeatureHolder();
	transient private RegionData regionData;
	
	private String volumeYaml;

	public Region(String type) {
		Class<? extends Volume> volumeType = Regions.getInstance().getVolume(name);
		if (volumeType != null) {
			try {
				volume = volumeType.newInstance();
				Yaml beanWriter = new Yaml();
				volumeYaml = "";
				volumeYaml = beanWriter.dump(volume);
			} catch (InstantiationException | IllegalAccessException ex) {
				Spout.getLogger().log(Level.SEVERE, null, ex);
			}
		}
		holder.setRegion(this);
		initData();
	}

	public Region(Class<? extends Volume> type) {
		try {
			volume = type.newInstance();
			Yaml beanWriter = new Yaml();
			volumeYaml = "";
			volumeYaml = beanWriter.dump(volume);
		} catch (InstantiationException | IllegalAccessException ex) {
			Spout.getLogger().log(Level.SEVERE, null, ex);
		}
		holder.setRegion(this);
		initData();
	}
	
	private void initData() {
		regionData = new RegionData(this);
	}

	public <T extends Feature> T add(Plugin plugin, Class<T> clazz) {
		return holder.add(plugin, clazz);
	}

	public <T extends Feature> void remove(Class<T> clazz) {
		holder.detach(clazz);
	}

	public <T extends Feature> T get(Class<T> clazz) throws FeatureNotFoundException {
		return holder.get(clazz);
	}

	/**
	 * Set the FeatureHolder for this RAZ, In this way, FeatureHolders can
	 * handle more than one RAZ.
	 *
	 * @param holder
	 */
	public void setHolder(FeatureHolder holder) {
		this.holder = holder;
	}

	/**
	 * Get the FeatureHolder for this RAZ
	 *
	 * @return FeatureHolder
	 */
	public FeatureHolder getHolder() {
		return holder;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public Volume getVolume() {
		return volume;
	}

	public UUID getUUID() {
		return ident;
	}

	public void setUUID(UUID value) {
		this.ident = value;
	}

	public String getWorldUUID() {
		return world;
	}
	
	public World getWorld() {
		return Spout.getEngine().getWorld(world, true);
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public ConcurrentList<PointMap> getPointCache() {
		return pointCache;
	}

	public void resetPointCache() {
		pointCache.clear();
	}

	public void addPointCache(PointMap point) {
		pointCache.add(point);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		regionData.load();
		in.defaultReadObject();
		Yaml loader = new Yaml(new RegionYamlConstructor());
		if (!"".equals(volumeYaml)) {
			volume = (Volume) loader.load(volumeYaml);
			volume.reInit();
		} else {
			volume = new VolumeBox();
		}
	}
	
	// HANDLE SERIALIZING VOLUME
	private void writeObject(ObjectOutputStream oos) throws IOException {
		regionData.save();

		Yaml beanWriter = new Yaml(new PointRepresenter());
		
		volumeYaml = "";

		volumeYaml = beanWriter.dump(volume);
			
		oos.defaultWriteObject();
	}
}
