package com.thedemgel.regions.volume.points;

/**
 * Types of POINTs to be used in Volume generation, and will eventually be used
 * in a form of Region creation walk-thru.
 *
 * The Enum is generic, and each volume type will require a Points Enum of it
 * own, to generate walk-thru/checks.
 */
public interface Points {

	/**
	 * The description of the point.
	 */
	String desc();
}
