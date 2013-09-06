package com.thedemgel.regions.annotations;

import com.thedemgel.regions.data.EventRegion;
import com.thedemgel.regions.feature.Feature;
import java.lang.reflect.Method;
import org.spout.api.event.Event;

public class RegionEventParser {
	public final void parse(Feature feature, Event event, EventRegion region) throws Exception {
		Method[] methods = feature.getClass().getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(RegionEvent.class)) {
				Class<?>[] params = method.getParameterTypes();
				if (params[0] == event.getClass()) {
					method.invoke(feature, event, region);
				}
			}
		}
	}
}
