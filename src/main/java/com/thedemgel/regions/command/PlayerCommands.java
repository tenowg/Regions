package com.thedemgel.regions.command;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.data.Volume;
import com.thedemgel.regions.feature.features.InRegion;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import me.dzineit.selectionapi.SelectionPlayer;
import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.Platform;

/**
 *
 * @author Craig <tenowg at thedemgel.com>
 */
public class PlayerCommands {

	private final Regions plugin;

	public PlayerCommands(Regions instance) {
		this.plugin = instance;
	}

	@Command(aliases = "pos1", usage = "[-b]", flags = "b", desc = "Select the first Position of a cube (use -b to select block)", min = 0, max = 0)
	public void pos1(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}

		SelectionPlayer comp = player.get(SelectionPlayer.class);
		PlayerRegionComponent preg = player.get(PlayerRegionComponent.class);
		
		comp.getSelection().setPos1(player.getScene().getPosition());
		
		if (args.hasFlag('b')) {
			preg.setPos1(true);			
		} else {
			preg.setPos1();
		}
		player.sendMessage(ChatStyle.CYAN, "Position One Set. ");
	}
	
	@Command(aliases = "pos2", usage = "[-b]", flags = "b", desc = "Select the second Position of a cube. (use -b to select block)", min = 0, max = 1)
	public void pos2(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		SelectionPlayer comp = player.get(SelectionPlayer.class);
		PlayerRegionComponent preg = player.get(PlayerRegionComponent.class);
		
		comp.getSelection().setPos2(player.getScene().getPosition());
		
		if (args.hasFlag('b')) {
			preg.setPos2(true);
			player.sendMessage("Adjusting...");
		} else {
			preg.setPos2();
		}
		
		player.sendMessage(ChatStyle.CYAN, "Position Two Set. ");
	}
	
	@Command(aliases = "updateregion", usage = "", desc = "Update Selected region")
	public void updateregion(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		if (player.get(PlayerRegionComponent.class).updateSelected() == null) {
			player.sendMessage("Either no region selected, or its a new region, try creating.");
			return;
		}
		
		player.sendMessage("Region updated.");
	}
	
	@Command(aliases = "createRegion", usage = "(name)", desc = "Create a Region. (Should have 2 Positions selected.", min = 1, max = 1)
	public void createRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		Region region = player.get(PlayerRegionComponent.class).createSelected(args.getString(0));
		
		if (region != null) {
			region.add(plugin, InRegion.class);
			player.sendMessage(ChatStyle.CYAN, "Region Created...");
		} else {
			player.sendMessage(ChatStyle.RED, "Region already exists, try updating instead.");
		}
	}
	
	@Command(aliases = "regiontypes", desc = "List all available region types.")
	public void regionsTypes(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		for (Entry<String, String> type : plugin.getTypeDesc().entrySet()) {
			player.sendMessage(type.getKey(), " - ", type.getValue());
		}
	}
	
	@Command(aliases = "settype", usage = "(type)",  desc = "Set the type of Volume for selected region.")
	public void setRegionType(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		Class<? extends Volume> volume = plugin.getVolume(args.getString(0));
		
		if (volume != null) {
			player.get(PlayerRegionComponent.class).setVolumeType(volume);
			player.sendMessage("Selection set to: " + volume.getSimpleName());
			return;
		}
		
		player.sendMessage("Volume Type not found.");
	}
	
	@Command(aliases = "selectregion", usage = "(name)", desc = "Select a region to edit it.")
	public void getRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		Region region = player.getWorld().getComponentHolder().get(WorldRegionComponent.class).getRegion(args.getString(0));
		
		if (region == null) {
			player.sendMessage(ChatStyle.RED, "No region exists by that name.");
			return;
		}
		
		player.get(PlayerRegionComponent.class).setSelectedRegion(region);
		player.sendMessage("Region Selected: " + region.getName());
	}
	
	@Command(aliases = "removeRegion", usage = "(name)", desc = "remove region information based on name.")
	public void removeRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		Region region = player.getWorld().getComponentHolder().get(WorldRegionComponent.class).getRegion(args.getString(0));
		
		if (region != null) {
			player.getWorld().getComponentHolder().get(WorldRegionComponent.class).removeRegion(region);
			player.sendMessage("Region Removed");
		} else {
			player.sendMessage("Region not found");
		}
	}
	
	@Command(aliases = "listRegion", usage = "(name)", desc = "remove region information based on name.")
	public void listRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		ConcurrentMap<UUID, Region> regions = player.getWorld().getComponentHolder().get(WorldRegionComponent.class).getRegions();
		
		for (Region region : regions.values()) {
			player.sendMessage(region.getName());
			player.sendMessage(region.getVolume().getMin() + "/" + region.getVolume().getMax());
		}
	}
}
