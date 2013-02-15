package com.thedemgel.regions.volume.points;

public enum PointsSphere implements Points {

	POS_CENTER("center") {
		@Override
		public String desc() {
			return "The first point of a BOX";
		}
	},
	POS_RADIUS("radius") {
		@Override
		public String desc() {
			return "The first point of a BOX";
		}
	};
	
	private String name;

	private PointsSphere(String value) {
		this.name = value;
	}
	
	public String getName() {
		return name;
	}
}
