
package com.thedemgel.regions.data;

import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;


public class VolumeBox extends Volume {
	private BBox box = new BBox(Vector3.ZERO, Vector3.ZERO);

	@Override
	public void init() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean containsPoint(Point point) {
		return box.containsPoint(point);
	}
	
	@Override
	public void setPoint(Points type, Vector3 point) {
		switch (type) {
			case POS_ONE: {
				box.setMin(point);
			}
			case POS_TWO: {
				box.setMax(point);
			}
		}
	}

	@Override
	public Vector3 getMin() {
		return box.getMin();
	}

	@Override
	public Vector3 getMax() {
		return box.getMax();
	}
	
	public BBox getBBox() {
		return box;
	}

	@Override
	public float getLowX() {
		return box.getLowX();
	}

	@Override
	public float getLowY() {
		return box.getLowY();
	}

	@Override
	public float getLowZ() {
		return box.getLowZ();
	}

	@Override
	public float getHighX() {
		return box.getHighX();
	}

	@Override
	public float getHighY() {
		return box.getHighY();
	}

	@Override
	public float getHighZ() {
		return box.getHighZ();
	}
}
