
package com.thedemgel.regions.feature.features;

import com.thedemgel.regions.feature.Feature;
import org.spout.api.event.Event;
import org.spout.api.event.player.PlayerChatEvent;


public class InRegion extends Feature {
	
	@Override
	public void execute(Event event) {
		if (!(event instanceof PlayerChatEvent)) {
			return;
		}
		
		PlayerChatEvent chatEvent = (PlayerChatEvent)event;
		chatEvent.getPlayer().sendMessage("You Chatted in this region");
		
	}
	
	@Override
	public void onTick(float dt) {
	}
}
