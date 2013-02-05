
package com.thedemgel.regions.feature;

import com.thedemgel.regions.data.Region;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.event.Event;


public class FeatureHolder implements Serializable {
	private ConcurrentMap<Class<? extends Feature>, Feature> features = new ConcurrentHashMap<Class<? extends Feature>, Feature>();

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
		} else {
			return null;
		}
		
		return (T) feature;
	}
	
	public <T extends Feature> T get(Class<T> clazz) {
		if (features.containsKey(clazz)) {
			return (T) features.get(clazz);
		}
		return null;
	}
	
	public void execute(Event event) {
		for (Feature feature : features.values()) {
			feature.execute(event);
		}
	}
	
	public void onTick(float dt) {
		for (Feature feature : features.values()) {
			feature.onTick(dt);
		}
	}
}
