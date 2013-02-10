package com.thedemgel.regions.annotations;

import com.thedemgel.regions.data.EventRegion;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import java.lang.reflect.Method;
import org.spout.api.Spout;
import org.spout.api.event.Event;

public class RegionEventParser {

	public void parse(Feature feature, Event event, Region region) throws Exception {
		Method[] methods = feature.getClass().getMethods();
		
		for (Method method : methods) {
			//Spout.getLogger().info(method.toString());
			if (method.isAnnotationPresent(RegionEvent.class)) {
				Class<?>[] params = method.getParameterTypes();
				//Spout.getLogger().info(event.getClass().toString());
				//for (Class<?> clazz : params) {
					//Spout.getLogger().info(clazz.toString());
				//}
				if (params[0] == event.getClass()) {
					Spout.getLogger().info(event.getClass().toString());
					method.invoke(feature, event, region);
				}
			}
		}
	}
	
	public void parse(Feature feature, Event event, EventRegion region) throws Exception {
		Method[] methods = feature.getClass().getMethods();
		
		for (Method method : methods) {
			//Spout.getLogger().info(method.toString());
			if (method.isAnnotationPresent(RegionEvent.class)) {
				Class<?>[] params = method.getParameterTypes();
				//Spout.getLogger().info(event.getClass().toString());
				//for (Class<?> clazz : params) {
					//Spout.getLogger().info(clazz.toString());
				//}
				if (params[0] == event.getClass()) {
					Spout.getLogger().info(event.getClass().toString());
					method.invoke(feature, event, region);
				}
			}
		}
	}
}
