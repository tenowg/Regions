package com.thedemgel.regions.volume.volumes;

import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import com.thedemgel.regions.volume.points.PointsBox;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.spout.api.Spout;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

public class VolumeBox extends Volume {

	// Adjusted Min/Max values
	private Vector3 pos1;
	private Vector3 pos2;
	
	private Vector3 minVol = Vector3.ZERO;
	private Vector3 maxVol = Vector3.ZERO;
	// Used for serialization
	public float minx;
	public float miny;
	public float minz;
	public float maxx;
	public float maxy;
	public float maxz;

	@Override
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

	@Override
	public Points getEnum(String type) {
		try {
			return PointsBox.valueOf(type);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public Points[] getEnum() {
		return PointsBox.values();
	}

	@Override
	public void setPoint(Points type, Vector3 point) {
		PointsBox points;
		if (type instanceof PointsBox) {
			points = (PointsBox) type;
		} else {
			Spout.getLogger().severe("Wrong type of ENUM of POINTS sent to VolumnBox.");
			return;
		}

		switch (points) {
			case ONE: {
				setMin(point);
				break;
			}
			case TWO: {
				setMax(point);
				break;
			}
		}
	}

	public Vector3 getAdjustedMin() {
		return pos1;
	}
	
	public Vector3 getAdjustedMax() {
		return pos2;
	}
	
	@Override
	public Vector3 getMin() {
		return minVol;
	}

	@Override
	public Vector3 getMax() {
		return maxVol;
	}

	@Override
	public void setMin(Vector3 point) {
		minVol = point;
		minx = minVol.getX();
		miny = minVol.getY();
		minz = minVol.getZ();
		adjustPos();
	}

	@Override
	public void setMax(Vector3 point) {
		maxVol = point;
		maxx = maxVol.getX();
		maxy = maxVol.getY();
		maxz = maxVol.getZ();
		adjustPos();
	}

	@Override
	public float getLowX() {
		if (pos1.getX() < pos2.getX()) {
			return pos1.getX();
		}

		return pos2.getX();
	}

	@Override
	public float getHighX() {
		if (pos1.getX() > pos2.getX()) {
			return pos1.getX();
		}

		return pos2.getX();
	}

	@Override
	public float getLowY() {
		if (pos1.getY() < pos2.getY()) {
			return pos1.getY();
		}

		return pos2.getY();
	}

	@Override
	public float getHighY() {
		if (pos1.getY() > pos2.getY()) {
			return pos1.getY();
		}

		return pos2.getY();
	}

	@Override
	public float getLowZ() {
		if (pos1.getZ() < pos2.getZ()) {
			return pos1.getZ();
		}

		return pos2.getZ();
	}

	@Override
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

	@Override
	public void reInit() {
		minVol = new Vector3(minx, miny, minz);
		maxVol = new Vector3(maxx, maxy, maxz);
		adjustPos();
	}

	@Override
	public Set<Points> validatePoints(ConcurrentMap<Points, Vector3> points) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
