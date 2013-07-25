
package com.thedemgel.regions.annotations;

import com.thedemgel.regions.data.Region;
import org.spout.api.command.CommandArguments;
import org.spout.api.entity.Player;


public class FeatureCommandArgs {
	private Player player;
	private CommandArguments args;
	private Region region;
	private String command;
	
	
	public FeatureCommandArgs(Player player, Region region, CommandArguments args) {
		this.player = player;
		this.args = args;
		this.region = region;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public CommandArguments getArgs() {
		return new CommandArguments("commands", args.get().subList(1, args.length()));
	}
	
	public String getCommand() {
			return args.get().get(0);
	}
	
	public Region getRegion() {
		return region;
	}
}
