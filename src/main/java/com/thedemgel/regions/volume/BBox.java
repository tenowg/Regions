package com.thedemgel.regions.volume;

import org.spout.api.collision.BoundingBox;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

public class BBox extends BoundingBox {

	private Vector3 pos1;
	private Vector3 pos2;

	public BBox(Vector3 pos1, Vector3 pos2) {
		super(pos1, pos2);
		adjustPos();
	}

	public boolean containsPoint(Point point) {
		if (getLowX() <= point.getX() && getHighX() >= point.getX()) {
			if (getLowY() <= point.getY() && getHighY() >= point.getY()) {
				if (getLowZ() <= point.getZ() && getHighZ() >= point.getZ()) {
					return true;
				}
			}
		}
		return false;
	}

	public Vector3 getActualMin() {
		return pos1;
	}
	
	public Vector3 getActualMax() {
		return pos2;
	}
	
	public void setMin(Vector3 vec) {
		this.min = vec;
		adjustPos();
	}

	public void setMax(Vector3 vec) {
		this.max = vec;
		adjustPos();
	}

	public float getLowX() {
		if (pos1.getX() < pos2.getX()) {
			return pos1.getX();
		}

		return pos2.getX();
	}

	public float getHighX() {
		if (pos1.getX() > pos2.getX()) {
			return pos1.getX();
		}

		return pos2.getX();
	}

	public float getLowY() {
		if (pos1.getY() < pos2.getY()) {
			return pos1.getY();
		}

		return pos2.getY();
	}

	public float getHighY() {
		if (pos1.getY() > pos2.getY()) {
			return pos1.getY();
		}

		return pos2.getY();
	}

	public float getLowZ() {
		if (pos1.getZ() < pos2.getZ()) {
			return pos1.getZ();
		}

		return pos2.getZ();
	}

	public float getHighZ() {
		if (pos1.getZ() > pos2.getZ()) {
			return pos1.getZ();
		}

		return pos2.getZ();
	}

	private void adjustPos() {
		Vector3 a = getMin();
		Vector3 b = getMax();

		float x, y, z;

		if (a.getX() > b.getX()) {
			x = (float) Math.ceil(a.getX());
		} else {
			x = (float) Math.floor(a.getX());
		}
		if (a.getY() > b.getY()) {
			y = (float) Math.ceil(a.getY());
		} else {
			y = (float) Math.floor(a.getY());
		}
		if (a.getZ() > b.getZ()) {
			z = (float) Math.ceil(a.getZ());
		} else {
			z = (float) Math.floor(a.getZ());
		}

		pos1 = new Vector3(x, y, z);

		if (b.getX() > a.getX()) {
			x = (float) Math.ceil(b.getX());
		} else {
			x = (float) Math.floor(b.getX());
		}
		if (b.getY() > a.getY()) {
			y = (float) Math.ceil(b.getY());
		} else {
			y = (float) Math.floor(b.getY());
		}
		if (b.getZ() > a.getZ()) {
			z = (float) Math.ceil(b.getZ());
		} else {
			z = (float) Math.floor(b.getZ());
		}

		pos2 = new Vector3(x, y, z);
	}
}
