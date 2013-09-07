
package com.thedemgel.regions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used to indicate a FIELD that should be saved to the
 * features configuration file.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Data {
	/**
	 * Converter used to convert custom Classes to yaml parsable objects.
	 */
	Class converter() default Object.class;
}
