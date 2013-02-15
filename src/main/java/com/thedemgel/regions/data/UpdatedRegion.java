
package com.thedemgel.regions.data;

import com.thedemgel.regions.volume.points.Points;
import java.util.HashSet;
import java.util.Set;


/**
 * The Object returned after a region is created or updated. Will return any 
 * positions that were not set, or if the region existed or not, and the region itself.
 */
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
	
	/**
	 * Whether or not the region was Successfully updated/created
	 * @return boolean if created/updated successfully
	 */
	public boolean getUpdated() {
		return updated;
	}
	
	/**
	 * Any positions that were not set when region was attempted
	 * to be created.
	 * @return 
	 */
	public Set<Points> getErrorPoints() {
		return errPoints;
	}
	
	public void addErr(Points errPoint) {
		errPoints.add(errPoint);
	}
	
	/**
	 * The Region that was attempted to be updated or created.
	 * @return created/updated Region
	 */
	public Region getRegion() {
		return region;
	}
	
	public void setExists(boolean exists) {
		this.exists = exists;
	}
	
	/**
	 * Did the region exist before it was submitted. This could mean
	 * the name already exists, the UUID was already set (as UUIDs are not set
	 * till creation)
	 * @return boolean if the region exists
	 */
	public boolean getExists() {
		return exists;
	}
}
