package com.thedemgel.regions.volume;

import com.thedemgel.regions.volume.points.Points;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.spout.api.geo.discrete.Point;
import org.spout.math.vector.Vector3;

public abstract class Volume {
	public Volume() {
	}

	public abstract boolean containsPoint(Point point);

	public final void setPoint(Points type, Point point) {
		this.setPoint(type, point);
	}

	public abstract void setPoint(Points type, Vector3 point);

	public abstract Set<Points> validatePoints(ConcurrentMap<Points, Vector3> points);
	
	public abstract Vector3 getMin();

	public abstract Vector3 getMax();

	public abstract void setMin(Vector3 point);

	public abstract void setMax(Vector3 point);

	public abstract float getLowX();

	public abstract float getLowY();

	public abstract float getLowZ();

	public abstract float getHighX();

	public abstract float getHighY();

	public abstract float getHighZ();

	public abstract void reInit();
	
	public abstract Points[] getEnum();
	
	public abstract Points getEnum(String name);
}
