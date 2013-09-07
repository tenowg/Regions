package com.thedemgel.regions.api.event;

import com.thedemgel.regions.data.Region;
import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.plugin.Plugin;

public class CreateRegionEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();
	private final Player player;
	private final Region region;
	private final Plugin plugin;

	public CreateRegionEvent(Player eventPlayer, Region eventRegion, Plugin eventPlugin) {
		this.player = eventPlayer;
		this.region = eventRegion;
		this.plugin = eventPlugin;
	}

	public CreateRegionEvent(Region eventRegion, Plugin eventPlugin) {
		this.player = null;
		this.region = eventRegion;
		this.plugin = eventPlugin;
	}

	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Use to cancel an event before it finishes.
	 * @param cancelled Boolean to cancel event (true = canceled)
	 */
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
