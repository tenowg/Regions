package com.thedemgel.regions;

import com.thedemgel.regions.annotations.EventOrder;
import com.thedemgel.regions.parser.WorldUUID;
import com.thedemgel.regions.parser.WorldPoint;
import com.thedemgel.regions.parser.EventParser;
import com.thedemgel.regions.annotations.RegionEvent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.feature.Feature;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import org.spout.api.event.Event;
import org.spout.api.event.EventExecutor;
import org.spout.api.event.Order;
import org.spout.api.exception.EventException;

public class RegisterEvents {

	private final Regions plugin;

	public RegisterEvents(Regions instance) {
		this.plugin = instance;
	}

	public final void registerEvents(Class<? extends Feature> feature, final EventParser parser) {
		Method[] methods = feature.getDeclaredMethods();

		for (final Method method : methods) {
			if (method.isAnnotationPresent(RegionEvent.class)) {
				final Class<?> checkClass = method.getParameterTypes()[0];
				final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);

				// Order processing\
				Order order = Order.LATE;
				if (method.isAnnotationPresent(EventOrder.class)) {
					order = method.getAnnotation(EventOrder.class).value();
				}

				EventExecutor executor = new EventExecutor() {
					@Override
					public void execute(Event event) throws EventException {
						try {
							if (!checkClass.isAssignableFrom(event.getClass())) {
								throw new EventException("Wrong event type passed to registered method");
							}

							//Method parseMethod = parser.getClass().getDeclaredMethod("parse", checkClass);
							Method parseMethod = parser.getClass().getMethod("parse", checkClass);
							if (parseMethod.getGenericReturnType().equals(WorldPoint.class)) {
								WorldPoint worldpoint = (WorldPoint) parseMethod.invoke(parser, event);
								worldpoint.getWorld().get(WorldRegionComponent.class).execute(event, worldpoint.getLoc());
							} else if (parseMethod.getGenericReturnType().equals(WorldUUID.class)) {
								WorldUUID worldpoint = (WorldUUID) parseMethod.invoke(parser, event);
								worldpoint.getWorld().get(WorldRegionComponent.class).execute(event, worldpoint.getUUID());
							}
						} catch (NoSuchMethodException e) {
							plugin.getLogger().log(Level.WARNING, "Event (" + checkClass + ") is missing from EventParser, please contact developer to add.");
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

				plugin.getEngine().getEventManager().registerEvent(eventClass, order, executor, plugin);
			}
		}
	}
}
