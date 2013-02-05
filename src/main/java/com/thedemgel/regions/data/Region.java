package com.thedemgel.regions.data;

import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.feature.FeatureHolder;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

public class Region implements Serializable {

	transient private BBox regionBox;
	private UUID ident;
	private String name;
	
	private Vector3 min;
	private Vector3 max;
	
	private FeatureHolder holder = new FeatureHolder();
	
	public Region(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		regionBox.set(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public Region(BBox region) {
		regionBox = region;
		min = regionBox.getMin();
		max = regionBox.getMax();
	}

	public <T extends Feature> T add(Class<T> clazz) {
		return holder.add(clazz);
	}
	
	public <T extends Feature> T get(Class<T> clazz) {
		return holder.get(clazz);
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
	
	public FeatureHolder getHolder() {
		return holder;
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		regionBox = new BBox(min, max);
	}
}
