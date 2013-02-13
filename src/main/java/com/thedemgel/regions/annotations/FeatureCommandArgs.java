
package com.thedemgel.regions.annotations;

import org.spout.api.command.CommandContext;
import org.spout.api.entity.Player;


public class FeatureCommandArgs {
	private Player player;
	private CommandContext args;
	
	
	public FeatureCommandArgs(Player player, CommandContext args) {
		this.player = player;
		this.args = args;
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
}
