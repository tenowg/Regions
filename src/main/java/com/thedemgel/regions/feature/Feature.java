
package com.thedemgel.regions.feature;

import java.io.Serializable;
import org.spout.api.event.Event;
import org.spout.api.event.Listener;


public class Feature implements Listener, Serializable {
	private FeatureHolder holder;

	/**
	 * Apply directly before this Feature is attached, returning false
	 * will invalidate the Attach and fail.
	 * @param holder
	 * @return 
	 */
	public boolean attachTo(FeatureHolder holder) {
		this.holder = holder;
		return true;
	}
	
	/**
	 * Executed immediately after attaching.
	 */
	public void onAttached() {
	}
	
	/**
	 * Executed immediately before detaching.
	 */
	public void onDetached() {
		
	}
	/**
	 * Gets the FeatureHolder this Feature is attached too.
	 * @return 
	 */
	public FeatureHolder getHolder() {
		return holder;
	}
	
	public void execute(Event event) {
	}
	
	public void onTick(float dt) {
	}
}
