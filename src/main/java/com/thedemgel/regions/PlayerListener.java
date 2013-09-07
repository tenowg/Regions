package com.thedemgel.regions;

import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;

import org.spout.api.Client;
import org.spout.api.Platform;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.engine.EngineStartEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.world.WorldLoadEvent;
import org.spout.api.geo.World;

/**
 * General Event Listener class for the Plugin.
 * TODO: change to GeneralListener
 */
public class PlayerListener implements Listener {
	/**
	 * The Regions base plugin instance.
	 */
	private Regions plugin;

	/**
	 * General Event Listener class for Regions.
	 * @param instance The base Regions plugin.
	 */
	public PlayerListener(Regions instance) {
		this.plugin = instance;
	}

	/**
	 * Event on player join to add PlayerRegionComponent.
	 * @param event PlayerJoinEvent from Spout Server
	 */
	@EventHandler(order = Order.LATE)
	public final void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.add(PlayerRegionComponent.class);
	}

	/**
	 * TODO: Unsure if this is still needed.
	 * To add PlayerRegionComponent if client is started
	 * in single player mode.
	 * @param event EngineStartEvent from Spout Server
	 */
	@EventHandler
	public final void onGameStart(EngineStartEvent event) {
		if (plugin.getEngine().getPlatform() != Platform.CLIENT) {
			return;
		}

		Player player = ((Client) plugin.getEngine()).getPlayer();
		player.add(PlayerRegionComponent.class);
	}

	/**
	 * Event to add WorldRegionComponent to a world on world load.
	 * TODO: add exclusion parameter.
	 * @param event WorldLoadEvent from Spout Server
	 */
	@EventHandler
	public final void onWorldLord(WorldLoadEvent event) {
		World world = event.getWorld();
		world.add(WorldRegionComponent.class).init();
	}
}
