
package com.thedemgel.regions.feature;

import java.io.Serializable;
import org.spout.api.event.Event;
import org.spout.api.event.Listener;


public class Feature implements Listener, Serializable {
	private FeatureHolder holder;

	public boolean attachTo(FeatureHolder holder) {
		this.holder = holder;
		return true;
	}
	
	public void onAttached() {
	}
	
	public FeatureHolder getHolder() {
		return holder;
	}
	
	public void execute(Event event) {
	}
	
	public void onTick(float dt) {
	}
}
