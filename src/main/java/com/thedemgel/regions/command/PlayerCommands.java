package com.thedemgel.regions.command;

import com.thedemgel.regions.ChatStyle;
import com.thedemgel.regions.Regions;
import com.thedemgel.regions.annotations.FeatureCommandArgs;
import com.thedemgel.regions.annotations.FeatureCommandParser;
import com.thedemgel.regions.api.RegionAPI;
import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.exception.InvalidPointPositionException;
import com.thedemgel.regions.exception.PointsNotSetException;
import com.thedemgel.regions.exception.RegionAlreadyExistsException;
import com.thedemgel.regions.exception.RegionNotFoundException;
import com.thedemgel.regions.exception.SelectionCancelledException;
import com.thedemgel.regions.exception.VolumeTypeNotFoundException;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import org.spout.api.Client;
import org.spout.api.Platform;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.Permissible;
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
	@Permissible("regions.command.position")
	public void pos(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		Points point;
		try {
			point = RegionAPI.setPosition(player, args.getString(0));
		} catch (InvalidPointPositionException ex) {
			player.sendMessage(ChatStyle.AQUA + "Position " + args.getString(0).toUpperCase() + " not Set. (Not an position value)");
			return;
		}
		
		player.sendMessage(ChatStyle.AQUA + "Position " + point + " set.");
	}
	
	@Command(aliases = "update", usage = "", desc = "Update Selected region")
	@Permissible("regions.command.update")
	public void updateregion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		/*UpdatedRegion ureg = player.get(PlayerRegionComponent.class).updateSelected();
		
		if (!ureg.getUpdated()) {
			for (Points point : ureg.getErrorPoints()) {
				player.sendMessage(ChatStyle.RED, point, " is not set. (", point.desc());
			}
			player.sendMessage(ChatStyle.RED, "Failed to Update Region.");
			return;
		}
		
		if (ureg.getExists()) {
			player.sendMessage(ChatStyle.RED, "Region already exists, try creating first.");
			return;
		} 
		
		player.sendMessage(ChatStyle.CYAN, "Region updated.");*/
		
		try {
			Region region = RegionAPI.updateRegion(player);
			player.sendMessage(ChatStyle.AQUA + "Region updated.");
		} catch (PointsNotSetException ex) {
			for (Points point : ex.getPoints()) {
				player.sendMessage(ChatStyle.RED + "" + point + " is not set. (" + point.desc());
			}
			player.sendMessage(ChatStyle.RED + "Failed to Update Region.");
		} catch (RegionNotFoundException ex) {
			player.sendMessage(ChatStyle.RED + "Region no found, try creating first.");
		}
	}
	
	@Command(aliases = "create", usage = "(name)", desc = "Create a Region. (Should have 2 Positions selected.", min = 1, max = 1)
	@Permissible("regions.command.create")
	public void createRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		/*UpdatedRegion ureg = player.get(PlayerRegionComponent.class).createSelected(args.getString(0));
		
		if (!ureg.getUpdated()) {
			// Do failed stuff
			for (Points point : ureg.getErrorPoints()) {
				player.sendMessage(ChatStyle.RED, point, " is not set. (", point.desc());
			}
			player.sendMessage(ChatStyle.RED, "Failed to Create Region.");
			return;
		}
		
		//Region region = ureg.getRegion();
		
		if (!ureg.getExists()) {
			player.sendMessage(ChatStyle.CYAN, "Region Created...");
		} else {
			player.sendMessage(ChatStyle.RED, "Region already exists, try updating instead.");
		}*/
		
		try {
			Region region = RegionAPI.createRegion(player);
			player.sendMessage(ChatStyle.AQUA + "Region created.");
		} catch (PointsNotSetException ex) {
			for (Points point : ex.getPoints()) {
				player.sendMessage(ChatStyle.RED + "" + point + " is not set. (" + point.desc());
			}
			player.sendMessage(ChatStyle.RED + "Failed to Create Region.");
		} catch (RegionAlreadyExistsException ex) {
			player.sendMessage(ChatStyle.RED + "Region already exists, try updating.");
		}
	}
	
	@Command(aliases = "types", desc = "List all available region types.")
	@Permissible("regions.command.types")
	public void regionsTypes(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		for (Entry<String, String> type : plugin.getTypeDesc().entrySet()) {
			player.sendMessage(type.getKey() + " - " + type.getValue());
		}
	}
	
	@Command(aliases = "settype", usage = "(type)",  desc = "Set the type of Volume for selected region.")
	@Permissible("regions.command.settype")
	public void setRegionType(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		/* Class<? extends Volume> volume = plugin.getVolume(args.getString(0));
		
		if (volume != null) {
			player.get(PlayerRegionComponent.class).setVolumeType(volume);
			player.sendMessage("Selection set to: " + volume.getSimpleName());
			return;
		}
		
		player.sendMessage("Volume Type not found.");*/
		
		try {
			Class<? extends Volume> volume = RegionAPI.setSelectedVolumeType(player, args.getString(0));
			player.sendMessage("Selection set to: " + volume.getSimpleName());
		} catch (VolumeTypeNotFoundException ex) {
			player.sendMessage("Volume Type not found.");
		}
	}
	
	@Command(aliases = "select", usage = "(name)", desc = "Select a region to edit it.")
	@Permissible("regions.command.select")
	public void getRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		try {
			Region region = RegionAPI.selectRegion(player, args.getString(0), plugin);
			player.sendMessage(ChatStyle.AQUA + "Region Selected: " + region.getName());
		} catch (RegionNotFoundException | SelectionCancelledException ex) {
			plugin.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
		} 
		
	}
	
	@Command(aliases = "remove", usage = "(name)", desc = "Remove region information based on name.")
	@Permissible("regions.command.remove")
	public void removeRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		/*Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(args.getString(0));
		
		if (region != null) {
			player.getWorld().get(WorldRegionComponent.class).removeRegion(region);
			player.sendMessage("Region Removed");
		} else {
			player.sendMessage("Region not found");
		}*/
		try {
			RegionAPI.removeRegion(player, args.getString(0));
			player.sendMessage("Region Removed");
		} catch (RegionNotFoundException ex) {
			player.sendMessage("Region not found");
		}
	}
	
	@Command(aliases = "new", desc = "Clear and initalize a new Region in a Players selection")
	@Permissible("regions.command.new")
	public void newRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		PlayerRegionComponent regComp = player.get(PlayerRegionComponent.class);
		regComp.newSelection();
		player.sendMessage(ChatStyle.AQUA + "New region put into player selection (Region not saved)");
		player.sendMessage(ChatStyle.AQUA + "Current volume type: " + regComp.getVolumeType().getSimpleName());
		player.sendMessage(ChatStyle.AQUA + "To choose a different type use: /raz types and /raz settype <type>");
		
		player.sendMessage(ChatStyle.YELLOW + "Please set each of the following values: ");
		
		for (Points type : regComp.getSelectedRegion().getVolume().getEnum()) {
			player.sendMessage(ChatStyle.YELLOW + "" + type + "" + ChatStyle.AQUA + " - " + type.desc());
		}
		
		
	}
	
	@Command(aliases = "points", desc = "List needed points in Region")
	@Permissible("regions.command.points")
	public void listpoints(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		PlayerRegionComponent regComp = player.get(PlayerRegionComponent.class);
		
		player.sendMessage(ChatStyle.YELLOW + "Please set each of the following values: ");
		
		for (Points type : regComp.getSelectedRegion().getVolume().getEnum()) {
			player.sendMessage(ChatStyle.YELLOW + "" + type + " - " + type.desc());
		}
	}
	
	@Command(aliases = "list", desc = "List all regions.")
	@Permissible("regions.command.list")
	public void listRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		ConcurrentMap<UUID, Region> regions = player.getWorld().get(WorldRegionComponent.class).getRegions();
		
		for (Region region : regions.values()) {
			player.sendMessage(region.getName());
			player.sendMessage(region.getVolume().getMin() + "/" + region.getVolume().getMax());
		}
	}
	
	@Command(aliases = {"listfeatures", "lf"}, usage = "[plugin]", desc = "Lists all the available Features.")
	@Permissible("regions.command.listfeatures")
	public void listFeatures(CommandSource source, CommandArguments args) throws CommandException {
		
	}
	
	@Command(aliases = "set", usage = "(feature) (command) [args...]", desc = "Sets or Executes a command on a Feature.")
	@Permissible("regions.command.set")
	public void set(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		Region region = player.get(PlayerRegionComponent.class).getSelectedRegion();
		
		List<String> test = new ArrayList<>();
		//test.addAll(args.getRawArgs().subList(2, args.getRawArgs().size()));
		
		test.addAll(args.get().subList(1, args.length()));
		System.out.println("PlayerCommands 266: " + test.get(0));
		
		CommandArguments newArgs = new CommandArguments(test);
		
		//CommandContext newArgs = new CommandContext(args.getString(1), test, args.getFlags());
		
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
