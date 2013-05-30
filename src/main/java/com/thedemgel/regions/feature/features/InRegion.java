
package com.thedemgel.regions.feature.features;

import com.thedemgel.regions.annotations.FeatureCommand;
import com.thedemgel.regions.annotations.FeatureCommandArgs;
import com.thedemgel.regions.annotations.Intensity;
import com.thedemgel.regions.annotations.OnTick;
import com.thedemgel.regions.annotations.RegionEvent;
import com.thedemgel.regions.annotations.RegionDetector;
import com.thedemgel.regions.data.EventRegion;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.feature.Tickable;
import com.thedemgel.regions.detectors.PlayerInRegion;
import org.spout.api.Spout;
import org.spout.api.chat.ChatSection;
import org.spout.api.event.player.PlayerChatEvent;


public class InRegion extends Feature implements Tickable {
	public int total_chats;
	
	/*
	 * Marking something as @RegionEvent will make this method
	 * fire everytime the event Type (in this case PlayerChatEvent) is
	 * passed to WorldRegionComponent and the point of origin is
	 * within the attached RAZ.
	 */
	@RegionEvent
	public void executeIt(PlayerChatEvent event, EventRegion region) {
		PlayerChatEvent chatEvent = (PlayerChatEvent)event;
		
		total_chats++;
		
		chatEvent.getPlayer().sendMessage("You Chatted in " + region.getRegion().getName() + " " + total_chats + " times.");
	}
	
	@OnTick(freq = 140)
	@RegionDetector({PlayerInRegion.class})
	public void tickTask(float dt, Region region) {
		// Will always run, as the default load is Intensity.IGNORE
		// Will only run every 140 ticks (2 seconds)
		System.out.println("Its runs: tickTask 140");
	}
	
	@OnTick(load = Intensity.LOW, freq = 400)
	public void someTask(float dt, Region region) {
		// Do Something on Tick, will not run if TPS is 8 or lower
		System.out.println("Its runs: tickTask 400");
	}
	
	@OnTick(load = Intensity.HIGHEST)
	public void anotherTask(float dt, Region region) {
		// So Something else on Tick, will not run if TPS is 16 or lower
	}
	
	@FeatureCommand(alias = "test")
	public void doSomething(FeatureCommandArgs args) {
		Spout.getLogger().info("Doing something");
		for (ChatSection arg : args.getArgs().getRawArgs()) {
			args.getPlayer().sendMessage(arg);
		}
	}
}
