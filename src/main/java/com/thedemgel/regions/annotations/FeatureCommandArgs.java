
package com.thedemgel.regions.annotations;

import com.thedemgel.regions.data.Region;
import org.spout.api.command.CommandContext;
import org.spout.api.entity.Player;


public class FeatureCommandArgs {
	private Player player;
	private CommandContext args;
	private Region region;
	
	
	public FeatureCommandArgs(Player player, Region region, CommandContext args) {
		this.player = player;
		this.args = args;
		this.region = region;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public CommandContext getArgs() {
		return args;
	}
	
	public String getCommand() {
		return args.getCommand();
	}
	
	public Region getRegion() {
		return region;
	}
}
