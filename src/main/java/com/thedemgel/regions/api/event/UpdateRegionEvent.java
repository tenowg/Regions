package com.thedemgel.regions.api.event;

import com.thedemgel.regions.data.Region;
import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.plugin.Plugin;


/**
 * The Event that will be called when a Region is updated through the API.
 * 
 * If a developer of a separate plugin wishes to call this when a plugin
 * updates a region, they are free to use the second Constructor without a
 * player. In this case a call to getPlayer() will return null.
 */
public class UpdateRegionEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();
	
	private final Player player;
	private final Region region;
	private final Plugin plugin;
	
	public UpdateRegionEvent(Player player, Region region, Plugin plugin) {
		this.player = player;
		this.region = region;
		this.plugin = plugin;
	}
	
	public UpdateRegionEvent(Region region, Plugin plugin) {
		this.player = null;
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

