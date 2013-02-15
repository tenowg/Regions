
package com.thedemgel.regions.data;

import com.thedemgel.regions.volume.points.Points;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;


public class UpdatedRegion {
	private boolean updated = true;
	private boolean exists = false;
	private Set<Points> errPoints = new HashSet<Points>();
	private Region region;
	
	public UpdatedRegion(Region region) {
		this.region = region;
	}
	
	public void setUpdated(boolean value) {
		updated = value;
	}
	
	public boolean getUpdated() {
		return updated;
	}
	
	public Set<Points> getErrorPoints() {
		return errPoints;
	}
	
	public void addErr(Points errPoint) {
		errPoints.add(errPoint);
	}
	
	public Region getRegion() {
		return region;
	}
	
	public void setExists(boolean exists) {
		this.exists = exists;
	}
	
	public boolean getExists() {
		return exists;
	}
}
