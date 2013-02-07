package com.thedemgel.regions.command;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.feature.features.InRegion;
import me.dzineit.selectionapi.SelectionPlayer;
import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
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

	@Command(aliases = "pos1", usage = "[exact | block] (not implemented)", desc = "Select the first Position of a cube", min = 0, max = 0)
	public void pos1(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}

		SelectionPlayer comp = player.get(SelectionPlayer.class);
		comp.getSelection().setPos1(player.getScene().getPosition());
		player.sendMessage(ChatStyle.CYAN, "Position One Set.");
	}
	
	@Command(aliases = "pos2", usage = "[exact | block] (not implemented)", desc = "Select the second Position of a cube.", min = 0, max = 0)
	public void pos2(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		SelectionPlayer comp = player.get(SelectionPlayer.class);
		comp.getSelection().setPos2(player.getScene().getPosition());
		player.sendMessage(ChatStyle.CYAN, "Position Two Set.");
	}
	
	@Command(aliases = "createRegion", usage = "(name)", desc = "Create a Region. (Should have 2 Positions selected.", min = 1, max = 1)
	public void createRegion(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		
		if (Spout.getPlatform() != Platform.CLIENT) {
			player = (Player) source;
		} else {
			player = ((Client) Spout.getEngine()).getActivePlayer();
		}
		
		Region region = player.getWorld().getComponentHolder().get(WorldRegionComponent.class).createRegion(player, args.getString(0));
		
		region.add(InRegion.class);
		player.sendMessage(ChatStyle.CYAN, "Region Created (Currently the Regions are ultra accurate (deminsions are to the float value of position)");
	}
	
	@Command(aliases = "getRegion", usage = "(name)", desc = "Get region information based on name.")
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
		
		player.sendMessage("Region: " + region.getName());
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
}
