package com.thedemgel.regions.parser;

import org.spout.api.event.block.BlockChangeEvent;
import org.spout.api.event.block.BlockEvent;
import org.spout.api.event.entity.AnimationEndEvent;
import org.spout.api.event.entity.EntityChangeWorldEvent;
import org.spout.api.event.entity.EntityDespawnEvent;
import org.spout.api.event.entity.EntityEvent;
import org.spout.api.event.entity.EntityHiddenEvent;
import org.spout.api.event.entity.EntityInteractBlockEvent;
import org.spout.api.event.entity.EntityInteractEntityEvent;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.entity.EntityShownEvent;
import org.spout.api.event.entity.EntitySpawnEvent;
import org.spout.api.event.entity.EntityTeleportEvent;
import org.spout.api.event.inventory.InventoryCloseEvent;
import org.spout.api.event.inventory.InventoryOpenEvent;
import org.spout.api.event.player.PlayerBanKickEvent;
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.player.PlayerKickEvent;
import org.spout.api.event.player.PlayerLeaveEvent;
import org.spout.api.event.player.PlayerLoginEvent;
import org.spout.api.event.player.PlayerWhitelistKickEvent;
import org.spout.api.event.player.input.PlayerClickEvent;
import org.spout.api.event.player.input.PlayerInputEvent;
import org.spout.api.event.player.input.PlayerKeyEvent;
import org.spout.api.event.world.EntityEnterWorldEvent;
import org.spout.api.event.world.EntityExitWorldEvent;

public class EventParser {
	// Spout event found in SpoutAPI

	// Block Events
	public WorldPoint parse(BlockChangeEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getBlock().getWorld());
		wp.setLoc(event.getBlock().getPosition());
		return wp;
	}
	
	public WorldPoint parse(BlockEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getBlock().getWorld());
		wp.setLoc(event.getBlock().getPosition());
		return wp;
	}
	
	// not applicable yet
	/*public WorldPoint parse(CuboidChangeEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getCuboid().getWorld());
		wp.setLoc(event.getBlock().getPosition());
		return wp;
	}*/
	
	// Chunk Events
	// Engine Events
	// Entity Events
	public WorldPoint parse(AnimationEndEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntityChangeWorldEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	/*public WorldPoint parse(EntityDeathEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}*/
	
	public WorldPoint parse(EntityDespawnEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntityEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntityHiddenEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntityInteractBlockEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntityInteractEntityEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntityInteractEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntityShownEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntitySpawnEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntityTeleportEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getEntity().getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	// Inventory Events
	public WorldPoint parse(InventoryCloseEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getViewer().getWorld());
		wp.setLoc(event.getViewer().getPhysics().getPosition());
		return wp;
	}
	
	//Not applicable yet
	/*public WorldPoint parse(InventoryEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getInventory()..getWorld());
		wp.setLoc(event.getViewer().getPhysics().getPosition());
		return wp;
	}*/
	
	public WorldPoint parse(InventoryOpenEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getOpener().getWorld());
		wp.setLoc(event.getOpener().getPhysics().getPosition());
		return wp;
	}
	
	// Player Input Events
	public WorldPoint parse(PlayerClickEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(PlayerInputEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(PlayerKeyEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	// Player Events
	// Not Applicable yet
	/*public WorldPoint parse(ClientPlayerConnectedEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event..getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}*/
	
	public WorldPoint parse(PlayerBanKickEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(PlayerChatEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	//Not Applicable yet
	/*public WorldPoint parse(PlayerConnectEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}*/
	
	public WorldPoint parse(PlayerEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	/*public WorldPoint parse(PlayerInteractEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}*/
	
	public WorldPoint parse(PlayerJoinEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(PlayerKickEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(PlayerLeaveEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(PlayerLoginEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(PlayerWhitelistKickEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getPlayer().getWorld());
		wp.setLoc(event.getPlayer().getPhysics().getPosition());
		return wp;
	}
	
	// Server Event
	// Server Economy Events
	// Server Access Events
	// Server Permissions Events
	// Server Plugin Events
	// Server Protection Events
	// Storage Events
	// World Events
	public WorldPoint parse(EntityEnterWorldEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
	public WorldPoint parse(EntityExitWorldEvent event) {
		WorldPoint wp = new WorldPoint();
		wp.setWorld(event.getWorld());
		wp.setLoc(event.getEntity().getPhysics().getPosition());
		return wp;
	}
	
}
