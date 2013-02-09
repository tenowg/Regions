package com.thedemgel.regions.data;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.feature.FeatureHolder;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.Spout;
import org.spout.api.math.Vector3;
import org.spout.api.util.list.concurrent.ConcurrentList;

public class Region implements Serializable {

	private final static long serialVersionUID = 32L;
	private Volume volume;
	private UUID ident = null;
	private String name;
	private UUID world;
	private ConcurrentList<PointMap> pointCache = new ConcurrentList<PointMap>();
	private FeatureHolder holder = new FeatureHolder();

	public Region(String type) {
		Class<? extends Volume> volumeType = Regions.getInstance().getVolume(name);
		if (volumeType != null) {
			try {
				volume = volumeType.newInstance();
			} catch (InstantiationException ex) {
				Spout.getLogger().log(Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				Spout.getLogger().log(Level.SEVERE, null, ex);
			}
		}
	}

	public Region(Class<? extends Volume> type) {
		try {
			volume = type.newInstance();
		} catch (InstantiationException ex) {
			Spout.getLogger().log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Spout.getLogger().log(Level.SEVERE, null, ex);
		}
	}

	public void setMin(Vector3 vec) {
		volume.setPoint(Points.POS_ONE, vec);
	}

	public void setMax(Vector3 vec) {
		volume.setPoint(Points.POS_TWO, vec);
	}

	public void setMinMax(Vector3 vec1, Vector3 vec2) {
		volume.setPoint(Points.POS_ONE, vec1);
		volume.setPoint(Points.POS_TWO, vec2);
	}

	public <T extends Feature> T add(Class<T> clazz) {
		return holder.add(clazz);
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
		try {
			/*Region region = (Region) in.readObject();
			for (Feature feature : region.holder.getFeatures().values()) {
				Spout.getLogger().info(feature.toString());
			}*/
			in.defaultReadObject();
			//in.readObject();
		} catch (InvalidClassException ex) {
			Spout.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
			
		}
	}
}
