package com.thedemgel.regions.data;

import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.feature.FeatureHolder;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import org.spout.api.geo.World;
import org.spout.api.math.Vector3;
import org.spout.api.util.list.concurrent.ConcurrentList;

public class Region implements Serializable {

	private final static long serialVersionUID = 32L;
	transient private BBox regionBox;
	private UUID ident = null;
	private String name;
	private UUID world;
	
	private Vector3 min;
	private Vector3 max;
	
	private ConcurrentList<PointMap> pointCache = new ConcurrentList<PointMap>();
	
	private FeatureHolder holder = new FeatureHolder();
	
	public Region(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		regionBox.set(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public Region(BBox region) {
		regionBox = region;
		min = regionBox.getMin();
		max = regionBox.getMax();
	}

	public void setMin(Vector3 vec) {
		regionBox.set(vec, max);
		min = regionBox.getMin();
	}
	
	public void setMax(Vector3 vec) {
		regionBox.set(min, vec);
		max = regionBox.getMax();
	}
	
	public void setMinMax(Vector3 vec1, Vector3 vec2) {
		regionBox.set(vec1, vec2);
		min = regionBox.getMin();
		max = regionBox.getMax();
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
	 * @param holder 
	 */
	public void setHolder(FeatureHolder holder) {
		this.holder = holder;
	}
	
	/**
	 * Get the FeatureHolder for this RAZ
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
	
	public BBox getRegion() {
		return regionBox;
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
		regionBox = new BBox(min, max);
		if (pointCache == null) {
			pointCache = new ConcurrentList<PointMap>();
		}
	}
}
