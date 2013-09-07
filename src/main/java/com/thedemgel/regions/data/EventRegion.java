package com.thedemgel.regions.data;

import java.util.Collections;
import java.util.Set;

public class EventRegion {

	private Set<Region> regionSet;
	private Region eventRegion;

	public EventRegion() {
	}

	public EventRegion(Set<Region> c, Region eventRegion) {
		regionSet = c;
		this.eventRegion = eventRegion;
	}

	public Set<Region> getRegionSet() {
		return Collections.unmodifiableSet(regionSet);
	}

	public Region getRegion() {
		return eventRegion;
	}
}
