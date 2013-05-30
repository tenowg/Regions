package com.thedemgel.regions;

import com.thedemgel.regions.annotations.RegionEvent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.feature.Feature;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.spout.api.event.Event;
import org.spout.api.event.EventExecutor;
import org.spout.api.event.Order;
import org.spout.api.exception.EventException;
import org.spout.api.geo.World;

public class RegisterEvents {

	public final Regions plugin;

	public RegisterEvents(Regions instance) {
		this.plugin = instance;
	}

	public void registerEvents(Class<? extends Feature> feature, final EventParser parser) {
		Method[] methods = feature.getDeclaredMethods();

		for (final Method method : methods) {
			if (method.isAnnotationPresent(RegionEvent.class)) {
				final Class<?> checkClass = method.getParameterTypes()[0];
				final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);

				EventExecutor executor = new EventExecutor() {
					@Override
					public void execute(Event event) throws EventException {
						try {
							if (!checkClass.isAssignableFrom(event.getClass())) {
								throw new EventException("Wrong event type passed to registered method");
							}
							
							Method parseMethod = parser.getClass().getDeclaredMethod("parse", checkClass);
							WorldPoint worldpoint = (WorldPoint) parseMethod.invoke(parser, event);
							
							worldpoint.getWorld().get(WorldRegionComponent.class).execute(event, worldpoint.getLoc());

						} catch (InvocationTargetException e) {
							if (e.getCause() instanceof EventException) {
								throw (EventException) e.getCause();
							}

							throw new EventException(e.getCause());
						} catch (Throwable t) {
							throw new EventException(t);
						}
					}
				};
				
				plugin.getEngine().getEventManager().registerEvent(eventClass, Order.LATE, executor, plugin);
			}
		}
	}
}
