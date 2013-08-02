package com.thedemgel.regions.command;

import com.thedemgel.regions.ChatStyle;
import com.thedemgel.regions.Regions;
import com.thedemgel.regions.annotations.FeatureCommandArgs;
import com.thedemgel.regions.annotations.FeatureCommandParser;
import com.thedemgel.regions.api.RegionAPI;
import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.PluginFeatures;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.exception.FeaturePermissionException;
import com.thedemgel.regions.exception.InvalidFeatureCommandException;
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
import org.spout.api.command.annotated.CommandDescription;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Plugin;

/**
 *
 * @author Craig <tenowg at thedemgel.com>
 */
public class PlayerCommands {

	private final Regions plugin;

	public PlayerCommands(Regions instance) {
		this.plugin = instance;
	}
	
	@CommandDescription(aliases = "info", usage = "[name]", desc = "Print out various information about a region.")
	@Permissible("regions.command.info")
	public void info(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.checkPlayer(source);
		String regionname = args.popString("regionname");
		
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionname);
		
		player.sendMessage("Name: " + region.getName());
		player.sendMessage("Id: " + region.getUUID().toString());
	}
	
	@CommandDescription(aliases = "pos", usage = "[point type]", desc = "Select the Position point of a volume.")
	@Permissible("regions.command.position")
	public void pos(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.checkPlayer(source);
		String pointType = args.popString("pointtype");
		
		Points point;
		try {
			point = RegionAPI.setPosition(player, pointType);
		} catch (InvalidPointPositionException ex) {
			player.sendMessage(ChatStyle.AQUA + "Position " + pointType.toUpperCase() + " not Set. (Not an position value)");
			return;
		}
		
		player.sendMessage(ChatStyle.AQUA + "Position " + point + " set.");
	}
	
	@CommandDescription(aliases = "update", usage = "", desc = "Update Selected region")
	@Permissible("regions.command.update")
	public void updateregion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		try {
			RegionAPI.updateRegion(player);
			player.sendMessage(ChatStyle.AQUA + "Region updated.");
		} catch (PointsNotSetException ex) {
			for (Points point : ex.getPoints()) {
				player.sendMessage(ChatStyle.RED + "" + point + " is not set. (" + point.desc());
			}
			player.sendMessage(ChatStyle.RED + "Failed to Update Region.");
		} catch (RegionNotFoundException ex) {
			player.sendMessage(ChatStyle.RED + "Region not found, try creating first.");
		}
	}
	
	@CommandDescription(aliases = "create", usage = "(name)", desc = "Create a Region. (Should have 2 Positions selected.")
	@Permissible("regions.command.create")
	public void createRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
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
	
	@CommandDescription(aliases = "types", desc = "List all available region types.")
	@Permissible("regions.command.types")
	public void regionsTypes(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		for (Entry<String, String> type : plugin.getTypeDesc().entrySet()) {
			player.sendMessage(type.getKey() + " - " + type.getValue());
		}
	}
	
	@CommandDescription(aliases = "settype", usage = "(type)",  desc = "Set the type of Volume for selected region.")
	@Permissible("regions.command.settype")
	public void setRegionType(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.checkPlayer(source);
		String type = args.popString("type");
		
		try {
			Class<? extends Volume> volume = RegionAPI.setSelectedVolumeType(player, type);
			player.sendMessage("Selection set to: " + volume.getSimpleName());
		} catch (VolumeTypeNotFoundException ex) {
			player.sendMessage("Volume Type not found.");
		}
	}
	
	@CommandDescription(aliases = "select", usage = "(name)", desc = "Select a region to edit it.")
	@Permissible("regions.command.select")
	public void getRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.checkPlayer(source);
		String regionName = args.popString("region");
		
		try {
			Region region = RegionAPI.selectRegion(player, regionName, plugin);
			player.sendMessage(ChatStyle.AQUA + "Region Selected: " + region.getName());
		} catch (RegionNotFoundException | SelectionCancelledException ex) {
			plugin.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
		} 
		
	}
	
	@CommandDescription(aliases = "remove", usage = "(name)", desc = "Remove region information based on name.")
	@Permissible("regions.command.remove")
	public void removeRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.checkPlayer(source);
		String regionName = args.popString("region");

		try {
			RegionAPI.removeRegion(player, regionName, plugin);
			player.sendMessage("Region Removed");
		} catch (RegionNotFoundException ex) {
			player.sendMessage("Region not found");
		}
	}
	
	@CommandDescription(aliases = "new", desc = "Clear and initalize a new Region in a Players selection")
	@Permissible("regions.command.new")
	public void newRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		RegionAPI.newRegion(player);
		
		PlayerRegionComponent regComp = player.get(PlayerRegionComponent.class);

		player.sendMessage(ChatStyle.AQUA + "New region put into player selection (Region not saved)");
		player.sendMessage(ChatStyle.AQUA + "Current volume type: " + regComp.getVolumeType().getSimpleName());
		player.sendMessage(ChatStyle.AQUA + "To choose a different type use: /raz types and /raz settype <type>");
		
		player.sendMessage(ChatStyle.YELLOW + "Please set each of the following values: ");
		
		for (Points type : regComp.getSelectedRegion().getVolume().getEnum()) {
			player.sendMessage(ChatStyle.YELLOW + "" + type + "" + ChatStyle.AQUA + " - " + type.desc());
		}
		
		
	}
	
	@CommandDescription(aliases = "points", desc = "List needed points in Region")
	@Permissible("regions.command.points")
	public void listpoints(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		PlayerRegionComponent regComp = player.get(PlayerRegionComponent.class);
		
		player.sendMessage(ChatStyle.YELLOW + "Please set each of the following values: ");
		
		for (Points type : regComp.getSelectedRegion().getVolume().getEnum()) {
			player.sendMessage(ChatStyle.YELLOW + "" + type + " - " + type.desc());
		}
	}
	
	@CommandDescription(aliases = "list", desc = "List all regions.")
	@Permissible("regions.command.list")
	public void listRegion(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		
		ConcurrentMap<UUID, Region> regions = player.getWorld().get(WorldRegionComponent.class).getRegions();
		
		for (Region region : regions.values()) {
			player.sendMessage(region.getName());
			player.sendMessage(region.getVolume().getMin() + "/" + region.getVolume().getMax());
		}
	}
	
	@CommandDescription(aliases = {"listfeatures", "lf"}, usage = "[plugin]", desc = "Lists all the available Features.")
	@Permissible("regions.command.listfeatures")
	public void listFeatures(CommandSource source, CommandArguments args) throws CommandException {
		Player player = getPlayer(source);
		for (Entry<Plugin, PluginFeatures> plugins : plugin.getFeatures().entrySet()) {
			player.sendMessage(ChatStyle.AQUA + "" + plugins.getKey().getName());
			for (Entry<String, Class<? extends Feature>> features : plugins.getValue().getFeatures().entrySet()) {
				player.sendMessage("    " + features.getValue().getSimpleName());
			}
		}
	}
	
	@CommandDescription(aliases = {"addfeature", "af"}, usage = "[plugin] [feature]", desc = "Attempts to add a Feature to a region.")
	@Permissible("regions.command.addfeature")
	public void addFeature(CommandSource source, CommandArguments args) throws CommandException {
		
	}
	
	@CommandDescription(aliases = {"set", "fc", "featurecommand"}, usage = "(feature) (command) [args...]", desc = "Sets or Executes a command on a Feature.")
	@Permissible("regions.command.set")
	public void set(CommandSource source, CommandArguments args) throws CommandException {
		Player player = args.checkPlayer(source);
		Region region = player.get(PlayerRegionComponent.class).getSelectedRegion();
		Feature feature = RegionArgumentTypes.popFeature("feature", args, region);
		
		List<String> test = new ArrayList<>();
		//test.addAll(args.getRawArgs().subList(2, args.getRawArgs().size()));
		
		test.addAll(args.get().subList(1, args.length()));
		//System.out.println("PlayerCommands 266: " + test.get(0));
		
		CommandArguments newArgs = new CommandArguments("set", test);
		
		//Feature feature = region.getHolder().get(featureName);
		
		FeatureCommandArgs newargs = new FeatureCommandArgs(player, region, newArgs);
		
		FeatureCommandParser parser = new FeatureCommandParser();
		try {
			parser.parse(feature, newargs);
		} catch (InvalidFeatureCommandException | FeaturePermissionException ex) {
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
