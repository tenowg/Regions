
package com.thedemgel.regions.volume.volumes;

import com.thedemgel.regions.volume.BSphere;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import com.thedemgel.regions.volume.points.PointsSphere;
import org.spout.api.Spout;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;


public class VolumeSphere extends Volume {
	
	private BSphere sphere = new BSphere(Vector3.ZERO, 0D);

	private Vector3 centerVol = new Vector3(Vector3.ZERO);
	private Vector3 radiusVol = new Vector3(Vector3.ZERO);
	
	private Vector3 minVol = new Vector3(Vector3.ZERO);
	private Vector3 maxVol = new Vector3(Vector3.ZERO);
	
	// Used for serialization
	public float minx;
	public float miny;
	public float minz;
	
	public float maxx;
	public float maxy;
	public float maxz;
	
	@Override
	public void setPoint(Points type, Vector3 point) {
		PointsSphere points;
		if (type instanceof PointsSphere) {
			points = (PointsSphere) type;
		} else {
			Spout.getLogger().severe("Wrong type of ENUM of POINTS sent to " + this.getClass().getSimpleName() + ".");
			return;
		}
		
		switch (points) {
			case CENTER: {
				setMin(point);
				break;
			}
			case RADIUS: {
				setMax(point);
				break;
			}
		}
		
		minVol = new Vector3(minx - centerVol.distance(radiusVol), miny -  centerVol.distance(radiusVol), minz -  centerVol.distance(radiusVol));
		maxVol = new Vector3(maxx - centerVol.distance(radiusVol), maxy -  centerVol.distance(radiusVol), maxx -  centerVol.distance(radiusVol));
		sphere = new BSphere(centerVol, centerVol.distance(radiusVol));
	}
	
	@Override
	public boolean containsPoint(Point point) {
		return sphere.containsPoint(point);
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
		centerVol = point;
		minx = centerVol.getX();
		miny = centerVol.getY();
		minz = centerVol.getZ();
	}
	
	@Override
	public void setMax(Vector3 point) {
		radiusVol = point;
		maxx = radiusVol.getX();
		maxy = radiusVol.getY();
		maxz = radiusVol.getZ();
	}
	
	public BSphere getBounding() {
		return sphere;
	}

	@Override
	public float getLowX() {
		if (getMin().getX() < getMax().getX()) {
			return getMin().getX();
		}
		
		return getMax().getX();
	}
	
	@Override
	public float getHighX() {
		if (getMin().getX() > getMax().getX()) {
			return getMin().getX();
		}
		
		return getMax().getX();
	}
	
	@Override
	public float getLowY() {
		if (getMin().getY() < getMax().getY()) {
			return getMin().getY();
		}
		
		return getMax().getY();
	}
	
	@Override
	public float getHighY() {
		if (getMin().getY() > getMax().getY()) {
			return getMin().getY();
		}
		
		return getMax().getY();
	}
	
	@Override
	public float getLowZ() {
		if (getMin().getZ() < getMax().getZ()) {
			return getMin().getZ();
		}
		
		return getMax().getZ();
	}
	
	@Override
	public float getHighZ() {
		if (getMin().getZ() > getMax().getZ()) {
			return getMin().getZ();
		}
		
		return getMax().getZ();
	}

	@Override
	public void reInit() {
		centerVol = new Vector3(minx, miny, minz);
		radiusVol = new Vector3(maxx, maxy, maxz);
		minVol = new Vector3(minx - centerVol.distance(radiusVol), miny -  centerVol.distance(radiusVol), minz -  centerVol.distance(radiusVol));
		maxVol = new Vector3(maxx - centerVol.distance(radiusVol), maxy -  centerVol.distance(radiusVol), maxx -  centerVol.distance(radiusVol));
		sphere = new BSphere(centerVol, centerVol.distance(radiusVol));
	}

	@Override
	public Points[] getEnum() {
		return PointsSphere.values();
	}

	@Override
	public Points getEnum(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
