
package com.thedemgel.regions.feature;

import com.thedemgel.regions.annotations.OnTickParser;
import com.thedemgel.regions.annotations.RegionEventParser;
import com.thedemgel.regions.data.Region;
import java.io.Serializable;
import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.event.Listener;


public class Feature implements Listener, Serializable {
	private static final long serialVersionUID = 31L;
	
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
	
	/**
	 * Calls the Event action on a Feature
	 * @param event 
	 * @param region
	 */
	public final void execute(Event event, Region region) {
		RegionEventParser parser = new RegionEventParser();
		try {
			parser.parse(this, event, region);
		} catch (Exception ex) {
			Spout.getLogger().info(ex.getMessage());
		}
	}
	
	/**
	 * Called Every tick, if there is no @OnTick in the features
	 * than nothing happens.
	 * @param dt
	 * @param region 
	 */
	public final void tick(float dt, Region region) {
		OnTickParser parser = new OnTickParser();
		try {
			parser.parse(this, dt, region);
		} catch (Exception ex) {
			Spout.getLogger().info(ex.getMessage());
		}
	}
}
