package com.thedemgel.regions.command;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.annotations.FeatureCommandArgs;
import com.thedemgel.regions.annotations.FeatureCommandParser;
import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.data.UpdatedRegion;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.feature.features.InRegion;
import com.thedemgel.regions.feature.features.Owner;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import org.spout.api.Client;
import org.spout.api.Platform;
import org.spout.api.chat.ChatSection;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

/**
 *
 * @author Craig <tenowg at thedemgel.com>
 */
public class PlayerCommands {

	private final Regions plugin;

	public PlayerCommands(Regions instance) {
		this.plugin = instance;
	}
	
	@Command(aliases = "pos", usage = "[point type]", desc = "Select the second Position of a cube. (use -b to select block)", min = 0, max = 1)
	@CommandPermissions("raz.command.position")
	public void pos(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		//SelectionPlayer comp = player.get(SelectionPlayer.class);
		PlayerRegionComponent preg = player.get(PlayerRegionComponent.class);
		
		//comp.getSelection().setPos2(player.getScene().getPosition());
		
		if (preg.setPos(args.getString(0), player.getScene().getPosition()) == null) {
			player.sendMessage(ChatStyle.CYAN, "Position ", args.getString(0).toUpperCase()," not Set. (Not an position value)");
		}
		
		player.sendMessage(ChatStyle.CYAN, "Position ", args.getString(0).toUpperCase()," Set. ");
	}
	
	@Command(aliases = "update", usage = "", desc = "Update Selected region")
	@CommandPermissions("raz.command.update")
	public void updateregion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		UpdatedRegion ureg = player.get(PlayerRegionComponent.class).updateSelected();
		
		if (!ureg.getUpdated()) {
			for (Points point : ureg.getErrorPoints()) {
				player.sendMessage(ChatStyle.RED, point, " is not set. (", point.desc());
			}
			player.sendMessage(ChatStyle.RED, "Failed to Update Region.");
			return;
		}
		
		if (ureg.getExists()) {
			player.sendMessage(ChatStyle.RED, "Region already exists, try updating.");
			return;
		} 
		
		player.sendMessage(ChatStyle.CYAN, "Region updated.");
	}
	
	@Command(aliases = "create", usage = "(name)", desc = "Create a Region. (Should have 2 Positions selected.", min = 1, max = 1)
	@CommandPermissions("raz.command.create")
	public void createRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		UpdatedRegion ureg = player.get(PlayerRegionComponent.class).createSelected(args.getString(0));
		
		if (!ureg.getUpdated()) {
			// Do failed stuff
			for (Points point : ureg.getErrorPoints()) {
				player.sendMessage(ChatStyle.RED, point, " is not set. (", point.desc());
			}
			player.sendMessage(ChatStyle.RED, "Failed to Create Region.");
			return;
		}
		
		Region region = ureg.getRegion();
		
		if (!ureg.getExists()) {
			region.add(plugin, InRegion.class);
			region.add(plugin, Owner.class);
			player.sendMessage(ChatStyle.CYAN, "Region Created...");
		} else {
			player.sendMessage(ChatStyle.RED, "Region already exists, try updating instead.");
		}
	}
	
	@Command(aliases = "types", desc = "List all available region types.")
	@CommandPermissions("raz.command.types")
	public void regionsTypes(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		for (Entry<String, String> type : plugin.getTypeDesc().entrySet()) {
			player.sendMessage(type.getKey(), " - ", type.getValue());
		}
	}
	
	@Command(aliases = "settype", usage = "(type)",  desc = "Set the type of Volume for selected region.")
	@CommandPermissions("raz.command.settype")
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
	@CommandPermissions("raz.command.select")
	public void getRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(args.getString(0));
		
		if (region == null) {
			player.sendMessage(ChatStyle.RED, "No region exists by that name.");
			return;
		}
		
		player.get(PlayerRegionComponent.class).setSelectedRegion(region);
		player.sendMessage("Region Selected: " + region.getName());
	}
	
	@Command(aliases = "remove", usage = "(name)", desc = "Remove region information based on name.")
	@CommandPermissions("raz.command.remove")
	public void removeRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(args.getString(0));
		
		if (region != null) {
			player.getWorld().get(WorldRegionComponent.class).removeRegion(region);
			player.sendMessage("Region Removed");
		} else {
			player.sendMessage("Region not found");
		}
	}
	
	@Command(aliases = "new", desc = "Clear and initalize a new Region in a Players selection")
	@CommandPermissions("raz.command.new")
	public void newRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		PlayerRegionComponent regComp = player.get(PlayerRegionComponent.class);
		regComp.newSelection();
		player.sendMessage(ChatStyle.CYAN, "New region put into player selection (Region not saved)");
		player.sendMessage(ChatStyle.CYAN, "Current volume type: " + regComp.getVolumeType().getSimpleName());
		player.sendMessage(ChatStyle.CYAN, "To choose a different type use: /raz types and /raz settype <type>");
		
		player.sendMessage(ChatStyle.YELLOW, "Please set each of the following values: ");
		
		for (Points type : regComp.getSelectedRegion().getVolume().getEnum()) {
			player.sendMessage(ChatStyle.YELLOW, type, ChatStyle.CYAN,  " - ", type.desc());
		}
		
		
	}
	
	@Command(aliases = "points", desc = "List needed points in Region")
	@CommandPermissions("raz.command.points")
	public void listpoints(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		PlayerRegionComponent regComp = player.get(PlayerRegionComponent.class);
		
		player.sendMessage(ChatStyle.YELLOW, "Please set each of the following values: ");
		
		for (Points type : regComp.getSelectedRegion().getVolume().getEnum()) {
			player.sendMessage(ChatStyle.YELLOW, type, " - ", type.desc());
		}
	}
	
	@Command(aliases = "list", desc = "List all regions.")
	@CommandPermissions("raz.command.list")
	public void listRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		ConcurrentMap<UUID, Region> regions = player.getWorld().get(WorldRegionComponent.class).getRegions();
		
		for (Region region : regions.values()) {
			player.sendMessage(region.getName());
			player.sendMessage(region.getVolume().getMin() + "/" + region.getVolume().getMax());
		}
	}
	
	@Command(aliases = "set", usage = "(feature) (command) [args...]", desc = "Sets or Executes a command on a Feature.")
	@CommandPermissions("raz.command.set")
	public void set(CommandContext args, CommandSource source) throws CommandException {
		Player player = getPlayer(source);
		
		Region region = player.get(PlayerRegionComponent.class).getSelectedRegion();
		
		List<ChatSection> test = new ArrayList<>();
		test.addAll(args.getRawArgs().subList(2, args.getRawArgs().size()));
		
		CommandContext newArgs = new CommandContext(args.getString(1), test, args.getFlags());
		
		Feature feature = region.getHolder().get(args.getString(0));
		
		FeatureCommandArgs newargs = new FeatureCommandArgs(player, region, newArgs);
		
		FeatureCommandParser parser = new FeatureCommandParser();
		try {
			parser.parse(feature, newargs);
		} catch (Exception ex) {
			plugin.getLogger().info(ex.toString());
		}
	}
	
	private Player getPlayer(CommandSource source) {
		Player player;
		
		if (plugin.getEngine().getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) plugin.getEngine()).getPlayer();
		}
		
		return player;
	}
}
