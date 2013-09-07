package com.thedemgel.regions.volume.points;

public enum PointsSphere implements Points {

	CENTER("The first point of a BOX"),
	RADIUS("The first point of a BOX");
	private String desc;

	private PointsSphere(String value) {
		this.desc = value;
	}

	@Override
	public String desc() {
		return desc;
	}
;
}
