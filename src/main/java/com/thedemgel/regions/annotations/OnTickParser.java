package com.thedemgel.regions.annotations;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.feature.Tickable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import org.spout.api.Spout;

/**
 * Parser to handle OnTick.
 *
 * @OnTick Any class that has a method with
 * @OnTick attached needs to implement Tickable interface.
 */
public class OnTickParser {
	private Integer TPS_LOWEST = 8;
	private Integer TPS_LOW = 10;
	private Integer TPS_MODERATE = 12;
	private Integer TPS_HIGH = 14;
	private Integer TPS_HIGHEST= 16;

	/**
	 * Handle the OnTick annotation.
	 * @param feature Feature being checked
	 * @param dt Tick duration
	 * @param region Region feature is in.
	 */
	public void parse(Feature feature, float dt, Region region) /*throws Exception*/ {
		if (!(feature instanceof Tickable)) {
			return;
		}

		Method[] methods = feature.getClass().getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(OnTick.class)) {

				OnTick ontick = method.getAnnotation(OnTick.class);

				if (!doTick(feature, method, ontick)) {
					continue;
				}

				switch (ontick.load()) {
					case LOWEST:
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < TPS_LOWEST) {
							continue;
						}
						break;
					case LOW:
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < TPS_LOW) {
							continue;
						}
						break;
					case MODERATE:
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < TPS_MODERATE) {
							continue;
						}
						break;
					case HIGH:
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < TPS_HIGH) {
							continue;
						}
						break;
					case HIGHEST:
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < TPS_HIGHEST) {
							continue;
						}
						break;
					default:
				}

				if (method.isAnnotationPresent(RegionDetector.class)) {
					RegionDetector anno = method.getAnnotation(RegionDetector.class);

					for (Class clazz : anno.value()) {
						feature.add(clazz).execute();
					}
				}

				try {
					method.invoke(feature, dt, region);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
					Spout.getLogger().log(Level.SEVERE, ex.getMessage());
				}
			}
		}
	}

	/**
	 * Check to see if enough ticks have passed since last time this method was fired.
	 * @param feature Feature being checked.
	 * @param method Method in feature being checked.
	 * @param ontick The on tick annotation.
	 * @return boolean If tick should happen.
	 */
	private boolean doTick(Feature feature, Method method, OnTick ontick) {

		Integer count = feature.incrementTimer(method.toString());

		Integer interval = ontick.freq();

		if (count % interval == 0) {
			feature.setTimer(method.toString(), 1);
			return true;
		}

		return false;
	}
}
