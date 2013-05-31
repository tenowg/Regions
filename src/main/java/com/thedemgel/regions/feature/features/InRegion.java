
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
import com.thedemgel.regions.detectors.PlayersInRegion;
import com.thedemgel.regions.events.EnterRegionEvent;
import com.thedemgel.regions.events.LeaveRegionEvent;
import org.spout.api.Spout;
import org.spout.api.chat.ChatSection;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerJoinEvent;


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
	
	@RegionEvent
	public void error(EnterRegionEvent event, EventRegion region) {
		System.out.println(event.getPlayer().getName() + " has entered " + region.getRegion().getName());
	}
	
	@RegionEvent
	public void leave(LeaveRegionEvent event, EventRegion region) {
		System.out.println(event.getPlayer().getName() + " has left " + region.getRegion().getName());
	}
	
	@OnTick(freq = 140)
	@RegionDetector({PlayersInRegion.class})
	public void tickTask(float dt, Region region) {
		// Will always run, as the default load is Intensity.IGNORE
		// Will only run every 140 ticks (2 seconds)
		System.out.println("Its runs: tickTask 140");
		for (Player player : get(PlayersInRegion.class).getPlayers()) {
			System.out.println(player.getName() + " is in region");
		}
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
