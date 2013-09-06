
package com.thedemgel.regions.volume.volumes;

import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.spout.api.geo.discrete.Point;
import org.spout.math.vector.Vector3;


public class VolumeConcavePoly extends Volume {

	@Override
	public boolean containsPoint(Point point) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Vector3 getMin() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Vector3 getMax() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setMin(Vector3 point) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setMax(Vector3 point) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getLowX() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getLowY() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getLowZ() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getHighX() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getHighY() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getHighZ() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void reInit() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Points[] getEnum() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Points getEnum(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setPoint(Points type, Vector3 point) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<Points> validatePoints(ConcurrentMap<Points, Vector3> points) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
