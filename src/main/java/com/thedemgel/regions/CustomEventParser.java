
package com.thedemgel.regions;

import com.thedemgel.regions.events.EnterRegionEvent;
import com.thedemgel.regions.events.LeaveRegionEvent;


public class CustomEventParser extends EventParser {
	// Custom Events
	public WorldPoint parse(EnterRegionEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getScene().getPosition());
		return wp;
	}
	
	public WorldUUID parse(LeaveRegionEvent event) {
		WorldUUID wp = new WorldUUID();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setUUID(event.getRegion().getUUID());
		return wp;
	}
}
