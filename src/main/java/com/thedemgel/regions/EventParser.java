package com.thedemgel.regions;

import org.spout.api.event.player.PlayerChatEvent;

public class EventParser {

	public WorldPoint parse(PlayerChatEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getScene().getPosition());
		return wp;
	}
}
