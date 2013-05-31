package com.thedemgel.regions.events;

import com.thedemgel.regions.data.Region;
import org.spout.api.entity.Player;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

public class EnterRegionEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	private Player player;
	private Region region;

	public EnterRegionEvent(Player player, Region region) {
		this.player = player;
		this.region = region;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Region getRegion() {
		return region;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
