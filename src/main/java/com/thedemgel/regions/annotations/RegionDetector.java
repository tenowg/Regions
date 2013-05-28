package com.thedemgel.regions.annotations;

import com.thedemgel.regions.detectors.Detector;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegionDetector {
	Class<? extends Detector>[] value();
}
