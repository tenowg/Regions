package com.thedemgel.regions.detectors;

public class PlayerInRegion extends Detector {

	private String test;

	public String getText() {
		return test;
	}

	@Override
	public void execute() {
		System.out.println("Detected");
	}
}
