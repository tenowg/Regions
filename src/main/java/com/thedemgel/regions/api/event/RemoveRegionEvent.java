package com.thedemgel.regions.api.event;

import com.thedemgel.regions.data.Region;
import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.plugin.Plugin;

public class RemoveRegionEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();
	private final Player player;
	private final Region region;
	private final Plugin plugin;

	public RemoveRegionEvent(Player player, Region region, Plugin plugin) {
		this.player = player;
		this.region = region;
		this.plugin = plugin;
	}

	public RemoveRegionEvent(Region region, Plugin plugin) {
		this.player = null;
		this.region = region;
		this.plugin = plugin;
	}

	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public final void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	public final Player getPlayer() {
		return player;
	}

	public final Region getRegion() {
		return region;
	}

	public final Plugin getPlugin() {
		return plugin;
	}
}
