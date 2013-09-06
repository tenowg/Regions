
package com.thedemgel.regions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * OnTick is a Annotation that needs to be placed on any method
 * in a Feature that needs to fire every tick, multiple methods within
 * a feature can have OnTick placed on them.
 * The OnTick annotation takes one optional argument of "load" to indicate how
 * much work this OnTick method does, and whether or not to process this method.
 * <p>
 * <code>@OnTick(load = Intensity.LOW, freq = 1)</code>
 * <p>
 * The default is Ignore which means this will happen every tick and doesn't
 * check the TPS of the server. To use the default you don't need to set
 * the argument
 * <p>
 * <code>@OnTick
 * public void someMethod(float dt, Region region)
 * </code>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnTick {
	/**
	 * The amount of load the annotated method is expected to place on the
	 * server.
	 */
	Intensity load() default Intensity.IGNORE;
	/**
	 * How often this method is allowed to be called by OnTick.
	 * The value is based on server ticks.
	 * TODO: Possibly change freq to another name.
	 */
	int freq() default 1;
}
