
package com.thedemgel.regions.api.event;

import com.thedemgel.regions.data.Region;
import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.plugin.Plugin;


public class SelectRegionEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();
	
	private final Player player;
	private final Region region;
	private final Plugin plugin;
	
	public SelectRegionEvent(Player player, Region region, Plugin plugin) {
		this.player = player;
		this.region = region;
		this.plugin = plugin;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	public Player getPlayer() {
		return player;
	}

	public Region getRegion() {
		return region;
	}

	public Plugin getPlugin() {
		return plugin;
	}
}
