
package com.thedemgel.regions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.spout.api.event.Order;

/**
 * EventOrder annotation tells the EventManager in what order you wish to
 * fire an event. The Order is the Spout Order ENUM.
 * <p>
 * <code>@EventOrder(Order.LATE)</code>
 * <p>
 * The default is Order.LATE and isn't required if you wish to keep the default.
 * <p>
 * <code>@EventOrder(Order.LATE)
 * public void someMethod(float dt, Region region)
 * </code>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventOrder {
	/**
	 * Order is the same as org.spout.api.event.Order.
	 */
	Order value() default Order.LATE;
}
