package com.thedemgel.regions.volume.points;

public enum PointsBox implements Points {

	ONE("The first point of a BOX"),
	TWO("The second point of a BOX");
	private String desc;

	private PointsBox(String value) {
		this.desc = value;
	}

	@Override
	public String desc() {
		return desc;
	}
}
