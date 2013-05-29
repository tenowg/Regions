package com.thedemgel.regions.annotations;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.feature.Tickable;
import java.lang.reflect.Method;

/**
 * Parser to handle
 *
 * @OnTick
 */
public class OnTickParser {

	public void parse(Feature feature, float dt, Region region) throws Exception {
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
					case LOWEST: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 8) {
							continue;
						}
					}
					case LOW: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 10) {
							continue;
						}
					}
					case MODERATE: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 12) {
							continue;
						}
					}
					case HIGH: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 14) {
							continue;
						}
					}
					case HIGHEST: {
						if (Regions.getInstance().getTPDMonitor().getAvgTPS() < 16) {
							continue;
						}
					}
				}

				method.invoke(feature, dt, region);
			}
		}
	}

	private boolean test;
	private boolean doTick(Feature feature, Method method, OnTick ontick) {
		//	while(test) {}
		//test = true;
		Integer count = feature.incrementTimer(method.toString());
		//feature.setTimer(method.toString(), count + 1);
		Integer interval = ontick.freq();

		if (count % interval == 0) {
			//System.out.println (count % interval);
			feature.setTimer(method.toString(), 1);
			//test = false;
			return true;
		}
//test = false;

		return false;
	}
}
