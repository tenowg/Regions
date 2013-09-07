package com.thedemgel.regions.annotations;

import com.thedemgel.regions.data.Region;
import org.spout.api.command.CommandArguments;
import org.spout.api.entity.Player;

public class FeatureCommandArgs {

	private Player player;
	private CommandArguments args;
	private Region region;
	//private String command;

	public FeatureCommandArgs(Player commandSource, Region regionSource, CommandArguments commandArguments) {
		this.player = commandSource;
		this.args = commandArguments;
		this.region = regionSource;
	}

	public final Player getPlayer() {
		return player;
	}

	public final CommandArguments getArgs() {
		return new CommandArguments("commands", args.get().subList(1, args.length()));
	}

	public final String getCommand() {
		return args.get().get(0);
	}

	public final Region getRegion() {
		return region;
	}
}
