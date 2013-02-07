
package com.thedemgel.regions.data;

import java.io.Serializable;
import org.spout.api.geo.discrete.Point;


public class PointMap implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int x, y, z;
	
	public PointMap(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public PointMap(Point point) {
		this.x = point.getBlockX() / 100;
		this.y = point.getBlockY() / 100;
		this.z = point.getBlockZ() / 100;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PointMap)) {
			return false;
		}
		PointMap p = (PointMap) obj;
		
		return this.x == p.x && this.y == p.y && this.z == p.z;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 19 * hash + this.x;
		hash = 19 * hash + this.y;
		hash = 19 * hash + this.z;
		return hash;
	}
	
	@Override
	public String toString() {
		return "PointMap {x=" + this.x + " y=" + this.y + " z=" + this.z + "}";
	}
}
