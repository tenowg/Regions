
package com.thedemgel.regions.feature;

import com.thedemgel.regions.annotations.OnTickParser;
import com.thedemgel.regions.annotations.RegionEventParser;
import com.thedemgel.regions.data.Region;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.Spout;
import org.spout.api.component.impl.DatatableComponent;
import org.spout.api.event.Event;
import org.spout.api.event.Listener;


/**
 * Features are executed from WorldRegionEvent, either onTick or based
 * on Events. Features are attached to a FeatureHolder, and FeatureHolders
 * are attached to RAZs (in the case of parent FeatureHolders, they are
 * attached to many RAZs.
 * @author tenowg
 */
public class Feature implements Listener, Serializable {
	private static final long serialVersionUID = 31L;
	
	private FeatureHolder holder;
	/**
	 * Apply directly before this Feature is attached, returning false
	 * will invalidate the Attach and fail.
	 * @param holder
	 * @return Successful attach
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
	 * @return The FeatureHolder this feature is attached.
	 */
	public FeatureHolder getHolder() {
		return holder;
	}
	
	public DatatableComponent getData() {
		return holder.getData();
	}
	
	/**
	 * Calls the Event action on a Feature
	 * @param event Any Event
	 * @param region The region the Event should fire.
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
	
	private void readObject(java.io.ObjectInputStream in) throws IOException {
		try {
			//Spout.getLogger().info("Test: " + in.readObject().toString());
			in.defaultReadObject();
			Spout.getLogger().info("I GOT THE ERROR HERE");
			
		} catch (InvalidClassException ex) {
			Spout.getLogger().log(Level.SEVERE, "I GOT THE ERROR HERE", ex);
			Spout.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
		} catch (ClassNotFoundException ex) {
			Spout.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
}
