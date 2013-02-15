package com.thedemgel.regions.volume.volumes;

import com.thedemgel.regions.volume.BBox;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import com.thedemgel.regions.volume.points.PointsBox;
import org.spout.api.Spout;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

public class VolumeBox extends Volume {
	//private static final long serialVersionUID = 2L;

	private BBox box = new BBox(Vector3.ZERO, Vector3.ZERO);
	private Vector3 minVol;
	private Vector3 maxVol;
	
	// Used for serialization
	public float minx;
	public float miny;
	public float minz;
	
	public float maxx;
	public float maxy;
	public float maxz;
	
	@Override
	public boolean containsPoint(Point point) {
		return box.containsPoint(point);
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
			case POS_ONE: {
				box.setMin(point);
				setMin(point);
				break;
			}
			case POS_TWO: {
				box.setMax(point);
				setMax(point);
				break;
			}
		}
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
	}
	
	@Override
	public void setMax(Vector3 point) {
		maxVol = point;
		maxx = maxVol.getX();
		maxy = maxVol.getY();
		maxz = maxVol.getZ();
	}
	
	public BBox getBounding() {
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

	@Override
	public void reInit() {
		minVol = new Vector3(minx, miny, minz);
		maxVol = new Vector3(maxx, maxy, maxz);
		box.setMin(minVol);
		box.setMax(maxVol);
	}
}
