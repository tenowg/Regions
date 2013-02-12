
package com.thedemgel.regions.feature.features;

import com.thedemgel.regions.annotations.Intensity;
import com.thedemgel.regions.annotations.OnTick;
import com.thedemgel.regions.annotations.RegionEvent;
import com.thedemgel.regions.data.EventRegion;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.logging.Level;
import org.spout.api.Spout;
import org.spout.api.event.player.PlayerChatEvent;


public class InRegion extends Feature {
	private String dumb = "This is dumb";
	
	private static final long serialVersionUID = 8L;
	
	/*
	 * Marking something as @RegionEvent will make this method
	 * fire everytime the event Type (in this case PlayerChatEvent) is
	 * passed to WorldRegionComponent and the point of origin is
	 * within the attached RAZ.
	 */
	@RegionEvent
	public void executeIt(PlayerChatEvent event, EventRegion region) {
		PlayerChatEvent chatEvent = (PlayerChatEvent)event;
		
		getData().put("talked", getData().get("talked", 0) + 1);
		
		chatEvent.getPlayer().sendMessage("You Chatted in " + region.getRegion().getName() + " " + getData().get("talked") + " times.");
	}
	
	@OnTick
	public void tickTask(float dt, Region region) {
		// Will always run, as the default load is Intensity.IGNORE
	}
	
	@OnTick(load = Intensity.LOW)
	public void someTask(float dt, Region region) {
		// Do Something on Tick, will not run if TPS is 8 or lower
	}
	
	@OnTick(load = Intensity.HIGHEST)
	public void anotherTask(float dt, Region region) {
		// So Something else on Tick, will not run if TPS is 16 or lower
	}
	
	public void setDumb(String value) {
		dumb = value;
	}
	
	public String getDumb() {
		return dumb;
	}
}
