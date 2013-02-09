
package com.thedemgel.regions.data;

import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

public abstract class Volume {
	public Volume() {}
	
	public abstract boolean containsPoint(Point point);
	
	public void setPoint(Points type, Point point) {}
	
	public void setPoint(Points type, Vector3 point) {}
	
	public abstract Vector3 getMin();
	
	public abstract Vector3 getMax();
	
	public abstract float getLowX();
	
	public abstract float getLowY();
	
	public abstract float getLowZ();
	
	public abstract float getHighX();
	
	public abstract float getHighY();
	
	public abstract float getHighZ();
}
