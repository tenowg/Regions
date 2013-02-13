
package com.thedemgel.regions.annotations;


/**
 * These are the values for Intensity, an ENUM that is used by the
 * OnTick annotation in Features. Each Intensity will regulate whether
 * a OnTick operation happens or not. The HIGHEST value will prevent
 * OnTicks from happening if the TPS of the server is lower than 16 while
 * the LOWEST will prevent onTicks from happening if the TPS is lower than 6.
 */
public enum Intensity {
	IGNORE,
	LOWEST,
	LOW,
	MODERATE,
	HIGH,
	HIGHEST
}
