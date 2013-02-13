package com.thedemgel.regions.command;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.annotations.FeatureCommandArgs;
import com.thedemgel.regions.annotations.FeatureCommandParser;
import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.feature.features.InRegion;
import com.thedemgel.regions.feature.features.Owner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dzineit.selectionapi.SelectionPlayer;
import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.chat.ChatSection;
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
		Player player = getPlayer(source);

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
		Player player = getPlayer(source);
		
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
	
	@Command(aliases = "update", usage = "", desc = "Update Selected region")
	public void updateregion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		if (player.get(PlayerRegionComponent.class).updateSelected() == null) {
			player.sendMessage("Either no region selected, or its a new region, try creating.");
			return;
		}
		
		player.sendMessage("Region updated.");
	}
	
	@Command(aliases = "create", usage = "(name)", desc = "Create a Region. (Should have 2 Positions selected.", min = 1, max = 1)
	public void createRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		Region region = player.get(PlayerRegionComponent.class).createSelected(args.getString(0));
		
		if (region != null) {
			region.add(plugin, InRegion.class);
			region.add(plugin, Owner.class);
			player.sendMessage(ChatStyle.CYAN, "Region Created...");
		} else {
			player.sendMessage(ChatStyle.RED, "Region already exists, try updating instead.");
		}
	}
	
	@Command(aliases = "types", desc = "List all available region types.")
	public void regionsTypes(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		for (Entry<String, String> type : plugin.getTypeDesc().entrySet()) {
			player.sendMessage(type.getKey(), " - ", type.getValue());
		}
	}
	
	@Command(aliases = "settype", usage = "(type)",  desc = "Set the type of Volume for selected region.")
	public void setRegionType(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		Class<? extends Volume> volume = plugin.getVolume(args.getString(0));
		
		if (volume != null) {
			player.get(PlayerRegionComponent.class).setVolumeType(volume);
			player.sendMessage("Selection set to: " + volume.getSimpleName());
			return;
		}
		
		player.sendMessage("Volume Type not found.");
	}
	
	@Command(aliases = "select", usage = "(name)", desc = "Select a region to edit it.")
	public void getRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		Region region = player.getWorld().getComponentHolder().get(WorldRegionComponent.class).getRegion(args.getString(0));
		
		if (region == null) {
			player.sendMessage(ChatStyle.RED, "No region exists by that name.");
			return;
		}
		
		player.get(PlayerRegionComponent.class).setSelectedRegion(region);
		player.sendMessage("Region Selected: " + region.getName());
	}
	
	@Command(aliases = "remove", usage = "(name)", desc = "Remove region information based on name.")
	public void removeRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		Region region = player.getWorld().getComponentHolder().get(WorldRegionComponent.class).getRegion(args.getString(0));
		
		if (region != null) {
			player.getWorld().getComponentHolder().get(WorldRegionComponent.class).removeRegion(region);
			player.sendMessage("Region Removed");
		} else {
			player.sendMessage("Region not found");
		}
	}
	
	@Command(aliases = "new", desc = "Clear and initalize a new Region")
	public void newRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		player.get(PlayerRegionComponent.class).newSelection();
		
	}
	
	@Command(aliases = "list", desc = "List all regions.")
	public void listRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		ConcurrentMap<UUID, Region> regions = player.getWorld().getComponentHolder().get(WorldRegionComponent.class).getRegions();
		
		for (Region region : regions.values()) {
			player.sendMessage(region.getName());
			player.sendMessage(region.getVolume().getMin() + "/" + region.getVolume().getMax());
		}
	}
	
	@Command(aliases = "set", usage = "(feature) (command) [args...]", desc = "Sets or Executes a command on a Feature.")
	public void set(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		Region region = player.get(PlayerRegionComponent.class).getSelectedRegion();
		
		List<ChatSection> test = new ArrayList<ChatSection>();
		test.addAll(args.getRawArgs().subList(2, args.getRawArgs().size()));
		
		CommandContext newArgs = new CommandContext(args.getString(1), test, args.getFlags());
		
		Feature feature = region.getHolder().get(args.getString(0));
		
		FeatureCommandArgs newargs = new FeatureCommandArgs(player, newArgs);
		
		FeatureCommandParser parser = new FeatureCommandParser();
		try {
			parser.parse(feature, newargs);
		} catch (Exception ex) {
			Spout.getLogger().info(ex.toString());
		}
	}
	
	private Player getPlayer(CommandSource source) {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		return player;
	}
}
