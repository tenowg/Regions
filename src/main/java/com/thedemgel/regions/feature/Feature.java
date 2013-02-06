
package com.thedemgel.regions.feature;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.annotations.OnTick;
import com.thedemgel.regions.annotations.OnTickParser;
import com.thedemgel.regions.data.Region;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.event.Listener;


public class Feature implements Listener, Serializable {
	private static final long serialVersionUID = 31L;
	//private OnTickParser parser;
	
	private FeatureHolder holder;
	/**
	 * Apply directly before this Feature is attached, returning false
	 * will invalidate the Attach and fail.
	 * @param holder
	 * @return 
	 */
	public boolean attachTo(FeatureHolder holder) {
		//parser = new OnTickParser();
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
	
	/**
	 * Calls the Event action on a Feature
	 * (Needs a more elegant way to pass region)
	 * @param event 
	 * @param region
	 */
	public void execute(Event event, Region region) {
	}
	
	
	public void tick(float dt) {
		OnTickParser parser = new OnTickParser();
		try {
			parser.parse(this, dt);
		} catch (Exception ex) {
			Spout.getLogger().info(ex.getMessage());
		}
	}
}
