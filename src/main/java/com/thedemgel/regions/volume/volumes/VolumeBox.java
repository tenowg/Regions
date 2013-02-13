package com.thedemgel.regions.volume.volumes;

import com.thedemgel.regions.volume.BBox;
import com.thedemgel.regions.data.Points;
import com.thedemgel.regions.volume.Volume;
import java.io.IOException;
import java.io.Serializable;
import org.spout.api.Spout;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

public class VolumeBox extends Volume implements Serializable {
	private static final long serialVersionUID = 2L;

	transient private BBox box = new BBox(Vector3.ZERO, Vector3.ZERO);
	private Vector3 min;
	private Vector3 max;

	@Override
	public boolean containsPoint(Point point) {
		return box.containsPoint(point);
	}

	@Override
	public void setPoint(Points type, Vector3 point) {
		switch (type) {
			case POS_ONE: {
				box.setMin(point);
				min = point;
				break;
			}
			case POS_TWO: {
				box.setMax(point);
				max = point;
				break;
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

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		
		box = new BBox(min, max);

		//setPoint(Points.POS_ONE, min);
		//setPoint(Points.POS_TWO, max);
	}
}
