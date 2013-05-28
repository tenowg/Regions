package com.thedemgel.regions.data;

import com.thedemgel.regions.Regions;
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
import org.spout.api.plugin.Plugin;
import org.spout.api.util.list.concurrent.ConcurrentList;
import org.yaml.snakeyaml.Yaml;

public class Region implements Serializable {

	private final static long serialVersionUID = 32L;
	transient private Volume volume;
	private UUID ident = null;
	private String name;
	private UUID world;
	private ConcurrentList<PointMap> pointCache = new ConcurrentList<>();
	private FeatureHolder holder = new FeatureHolder();
	
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
	}

	public <T extends Feature> T add(Plugin plugin, Class<T> clazz) {
		return holder.add(plugin, clazz);
	}

	public <T extends Feature> void remove(Class<T> clazz) {
		holder.detach(clazz);
	}

	public <T extends Feature> T get(Class<T> clazz) {
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
	 * @return
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

	public UUID getWorld() {
		return world;
	}

	public void setWorld(UUID world) {
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
		in.defaultReadObject();
		Yaml loader = new Yaml(new RegionYamlConstructor());
		Spout.getLogger().info(volumeYaml);
		if (!"".equals(volumeYaml)) {
			volume = (Volume) loader.load(volumeYaml);
			volume.reInit();
		} else {
			volume = new VolumeBox();
		}
	}
	
	// HANDLE SERIALIZING VOLUME
	private void writeObject(ObjectOutputStream oos) throws IOException {

		Yaml beanWriter = new Yaml(new PointRepresenter());
		
		volumeYaml = "";

		volumeYaml = beanWriter.dump(volume);
		
		Spout.getLogger().info(volumeYaml);
			
		oos.defaultWriteObject();
	}
}
