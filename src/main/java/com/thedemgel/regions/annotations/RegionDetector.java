package com.thedemgel.regions.annotations;

import com.thedemgel.regions.detectors.Detector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate a Detector class to be executed before the method.
 * 
 * Once executed the Detector will be attached to the Feature to be retrieved later by the method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegionDetector {
	/**
	 * A Class that extends Detector and is intended to be fired before the method is executed.
	 */
	Class<? extends Detector>[] value();
}
