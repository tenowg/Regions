
package com.thedemgel.regions.annotations;

import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.exception.InvalidFeatureCommandException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.command.CommandArguments;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;


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
		return new CommandArguments(args.get().subList(1, args.length()));
	}
	
	public String getCommand() throws InvalidFeatureCommandException {
		try {
			return args.getString(0);
		} catch (CommandException ex) {
			throw new InvalidFeatureCommandException("Invalid Feature Command");
		}
	}
	
	public Region getRegion() {
		return region;
	}
}
