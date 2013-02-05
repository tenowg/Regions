
package com.thedemgel.regions.data;

import java.io.Serializable;
import org.spout.api.collision.BoundingBox;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;


public class BBox extends BoundingBox implements Serializable {
	
	public BBox(Vector3 pos1, Vector3 pos2) {
		super(pos1, pos2);
	}
	
	public boolean containsPoint(Point point) {
		if (getLowX() < point.getX() && getHighX() > point.getX()) {
			if (getLowY() < point.getY() && getHighY() > point.getY()) {
				if (getLowZ() < point.getZ() && getHighZ() > point.getZ()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public float getLowX() {
		if (getMin().getX() < getMax().getX()) {
			return getMin().getX();
		}
		
		return getMax().getX();
	}
	
	public float getHighX() {
		if (getMin().getX() > getMax().getX()) {
			return getMin().getX();
		}
		
		return getMax().getX();
	}
	
	public float getLowY() {
		if (getMin().getY() < getMax().getY()) {
			return getMin().getY();
		}
		
		return getMax().getY();
	}
	
	public float getHighY() {
		if (getMin().getY() > getMax().getY()) {
			return getMin().getY();
		}
		
		return getMax().getY();
	}
	
	public float getLowZ() {
		if (getMin().getZ() < getMax().getZ()) {
			return getMin().getZ();
		}
		
		return getMax().getZ();
	}
	
	public float getHighZ() {
		if (getMin().getZ() > getMax().getZ()) {
			return getMin().getZ();
		}
		
		return getMax().getZ();
	}
}
