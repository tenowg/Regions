
package com.thedemgel.regions.feature;

import com.thedemgel.regions.annotations.FeatureCommandArgs;
import com.thedemgel.regions.annotations.OnTickParser;
import com.thedemgel.regions.annotations.RegionEventParser;
import com.thedemgel.regions.data.EventRegion;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.detectors.Detector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.plugin.Plugin;


/**
 * Features are executed from WorldRegionEvent, either onTick or based
 * on Events. Features are attached to a FeatureHolder, and FeatureHolders
 * are attached to RAZs (in the case of parent FeatureHolders, they are
 * attached to many RAZs.
 * @author tenowg
 */
public class Feature {
	protected Plugin plugin;
	private String pluginName;
	
	private ConcurrentMap<Class<? extends Detector>, Detector> detectors = new ConcurrentHashMap<>();
	
	private FeatureHolder holder;
	/**
	 * Apply directly before this Feature is attached, returning false
	 * will invalidate the Attach and fail.
	 * @param holder
	 * @return Successful attach
	 */
	public boolean attachTo(Plugin plugin, FeatureHolder holder) {
		this.holder = holder;
		this.plugin = plugin;
		this.pluginName = plugin.getName();
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
	
	/**
	 * Calls the Event action on a Feature
	 * @param event Any Event
	 * @param region The region the Event should fire.
	 */
	/*public final void execute(Event event, Region region) {
		RegionEventParser parser = new RegionEventParser();
		try {
			//parser.parse(this, event, region);
		} catch (Exception ex) {
			Spout.getLogger().info(ex.getMessage());
		}
	}*/

	/**
	 * Calls the Event action on a Feature
	 * @param event Any Event
	 * @param region The region the Event should fire.
	 */
	public final void execute(Event event, EventRegion region) {
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
	
	/**
	 * Gets the name of the plugin that manages this Feature.
	 * @return String name of plugin
	 */
	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String value) {
		this.pluginName = value;
	}
	
	/**
	 * This method should be overridden if you want special permissions checks
	 * for FeatureCommandPermission, base it will just check for
	 * <code>raz.feature.{regionname}.{stringpermission}</code>
	 * regionname if a lowercase version of the actual region name.
	 * 
	 * @param stringpermission
	 * @param command
	 * @return 
	 */
	public boolean hasPermission(String stringpermission, FeatureCommandArgs command) {
		// TODO: find a way to make this work for ParentFeatures and regions
		// Currently only works based on regions.
		// Probably something like "if holder is ParentFeatureHolder getParentName"
		String perm;
		if ("".equals(stringpermission)) {
			perm = "raz.feature." + command.getRegion().getName().toLowerCase();
		} else {
			perm = "raz.feature." + command.getRegion().getName().toLowerCase() + "." + stringpermission;
		}
		return command.getPlayer().hasPermission(perm);
	}
	
	public <T extends Detector> T get(Class<T> clazz) {
		if (detectors.containsKey(clazz)) {
			return (T) detectors.get(clazz);
		}
		
		return null;
	}
	
	public <T extends Detector> T add(Class<T> clazz) {
		if (detectors.containsKey(clazz)) {
			return (T) detectors.get(clazz);
		}
		
		Detector detector = null;
		try {
			detector = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			Spout.getLogger().log(Level.SEVERE, "Error create Detector Class " + clazz.getName());
		}
		
		detectors.put(clazz, detector);
		return (T) detector;
	}
}
