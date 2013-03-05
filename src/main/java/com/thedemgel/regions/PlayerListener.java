
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
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.world.WorldLoadEvent;
import org.spout.api.geo.World;

public class PlayerListener implements Listener {
	private Regions plugin;
	
	public PlayerListener(Regions instance) {
		this.plugin = instance;
	}
	
	@EventHandler(order = Order.LATE)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.add(PlayerRegionComponent.class);
	}
	
	@EventHandler
	public void onGameStart(EngineStartEvent event) {
		if (plugin.getEngine().getPlatform() != Platform.CLIENT) {
			return;
		}

		Player player = ((Client) plugin.getEngine()).getActivePlayer();
		player.add(PlayerRegionComponent.class);
	}

	@EventHandler
	public void onWorldLoard(WorldLoadEvent event) {
		World world = event.getWorld();
		world.add(WorldRegionComponent.class).init();
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		event.getPlayer().getWorld().get(WorldRegionComponent.class).execute(event, event.getPlayer().getScene().getPosition());
	}
}
