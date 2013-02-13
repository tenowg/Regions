package com.thedemgel.regions.annotations;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.feature.Tickable;
import java.lang.reflect.Method;

/**
 * Parser to handle @OnTick
 */
public class OnTickParser {

	public void parse(Feature feature, float dt, Region region) throws Exception {
		if (!(feature instanceof Tickable)) {
			return;
		}
		
		Method[] methods = feature.getClass().getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(OnTick.class)) {
				OnTick intensity = method.getAnnotation(OnTick.class);
				switch (intensity.load()) {
					case LOWEST: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 8) {
							return;
						}
					}
					case LOW: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 10) {
							return;
						}
					}
					case MODERATE: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 12) {
							return;
						}
					}
					case HIGH: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 14) {
							return;
						}
					}
					case HIGHEST: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 16) {
							return;
						}
					}
				}

				method.invoke(feature, dt, region);
			}
		}
	}
}
