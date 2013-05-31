package com.thedemgel.regions.exception;

import com.thedemgel.regions.volume.points.Points;
import java.util.Set;

public class PointsNotSetException extends Exception {

	private Set<Points> points;

	public PointsNotSetException() {
	}

	public PointsNotSetException(Set<Points> points) {
		this.points = points;
	}

	public Set<Points> getPoints() {
		return points;
	}
}
