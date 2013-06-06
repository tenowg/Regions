
package com.thedemgel.regions.api.event;

import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.plugin.Plugin;


public class AddFeatureEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();

	private final Player player;
	private final Region region;
	private final Feature feature;
	private final Plugin plugin;
	
	public AddFeatureEvent(Player player, Region region, Feature feature, Plugin plugin) {
		this.player = player;
		this.region = region;
		this.feature = feature;
		this.plugin = plugin;
	}
	
	public AddFeatureEvent(Region region, Feature feature, Plugin plugin) {
		this.player = null;
		this.region = region;
		this.feature = feature;
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

	public Feature getFeature() {
		return feature;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
}
