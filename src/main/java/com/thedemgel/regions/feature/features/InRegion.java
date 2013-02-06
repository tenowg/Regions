
package com.thedemgel.regions.feature.features;

import com.thedemgel.regions.annotations.Intensity;
import com.thedemgel.regions.annotations.OnTick;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.event.player.PlayerChatEvent;


public class InRegion extends Feature {
	private static final long serialVersionUID = 66L;

	@Override
	public void execute(Event event, Region region) {
		if (!(event instanceof PlayerChatEvent)) {
			return;
		}
		
		PlayerChatEvent chatEvent = (PlayerChatEvent)event;
		chatEvent.getPlayer().sendMessage("You Chatted in " + region.getName());
		
	}
	
	@OnTick(load = Intensity.LOW)
	public void someTask(float dt) {
		// Do Something on Tick, will not run if TPS is 8 or lower
	}
	
	@OnTick(load = Intensity.HIGHEST)
	public void anotherTask(float dt) {
		// So Something else on Tick, will not run if TPS is 16 or lower
	}
}
