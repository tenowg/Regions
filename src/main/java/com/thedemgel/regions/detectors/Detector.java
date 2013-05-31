
package com.thedemgel.regions.detectors;

import com.thedemgel.regions.feature.Feature;


public abstract class Detector {

	private Feature feature;
	
	public abstract void execute();
	
	public final Feature getFeature() {
		return feature;
	}
	
	public final void setFeature(Feature feature) {
		this.feature = feature;
	}
}
