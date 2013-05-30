package com.thedemgel.regions;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;

public class WorldPoint {

	private World world;
	private Point loc;

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Point getLoc() {
		return loc;
	}

	public void setLoc(Point loc) {
		this.loc = loc;
	}
}
