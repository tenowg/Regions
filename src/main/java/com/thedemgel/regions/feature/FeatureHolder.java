
package com.thedemgel.regions.feature;

import com.thedemgel.regions.data.Region;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.event.Event;
import org.spout.api.util.list.concurrent.ConcurrentList;


public class FeatureHolder implements Serializable {
	private static final long serialVersionUID = 56L;
	private ConcurrentMap<Class<? extends Feature>, Feature> features = new ConcurrentHashMap<Class<? extends Feature>, Feature>();
	private ConcurrentList<FeatureHolder> parentFeatures = new ConcurrentList<FeatureHolder>();
	
	/**
	 * Add a Feature to this FeatureHolder
	 * @param clazz
	 * @return
	 */
	public <T extends Feature> T add(Class<T> clazz) {
		Feature feature = null;
		try {
			feature = clazz.newInstance();
		} catch (InstantiationException ex) {
			Logger.getLogger(Region.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(Region.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		if (feature == null) {
			return null;
		}
		
		if(feature.attachTo(this)) {
			features.put(clazz, feature);
			feature.onAttached();
		} else {
			return null;
		}
		
		return (T) feature;
	}
	
	/**
	 * Retrieve a Feature that is attached to this FeatureHolder
	 * @param clazz
	 * @return 
	 */
	public <T extends Feature> T get(Class<T> clazz) {
		if (features.containsKey(clazz)) {
			return (T) features.get(clazz);
		}
		return null;
	}
	
	/**
	 * Remove a Feature that is attached to this FeatureHolder
	 * @param clazz
	 */
	public <T extends Feature> void detach(Class<T> clazz) {
		features.get(clazz).onDetached();
		features.remove(clazz);
	}
	
	/**
	 * Add a parent FeatureHolder to holder.
	 * @param holder 
	 */
	public void addFeatureHolder(FeatureHolder holder) {
		parentFeatures.add(holder);
	}
	
	/**
	 * Remove a Parent FeatureHolder from this FeatureHolder.
	 * @param holder 
	 */
	public void removeFeatureHolder(FeatureHolder holder) {
		parentFeatures.remove(holder);
	}
	
	/**
	 * Executes all execute methods while passing EVENT to all Features.
	 * @param event 
	 */
	public void execute(Event event) {
		for (FeatureHolder parent : parentFeatures) {
			parent.execute(event);
			for (Entry<Class<? extends Feature>, Feature> feature : parent.features.entrySet()) {
				if (!features.containsKey(feature.getKey())) {
					feature.getValue().execute(event);
				}
			}
		}
		
		for (Feature feature : features.values()) {
			feature.execute(event);
		}
	}
	
	public void onTick(float dt) {
		for (FeatureHolder parent : parentFeatures) {
			parent.onTick(dt);
		}
		
		for (Feature feature : features.values()) {
			feature.onTick(dt);
		}
	}
}
