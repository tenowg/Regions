package com.thedemgel.regions.command;

import com.thedemgel.regions.Regions;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.NestedCommand;
import org.spout.api.exception.CommandException;

public class RazCommand {

	private final Regions plugin;

	public RazCommand(Regions instance) {
		this.plugin = instance;
	}

	// This is the command. Will detail all the options later.
	@Command(aliases = {"region", "raz"}, usage = "", desc = "Access region commands")
	// This is the class with all subcommands under /config
	@NestedCommand(PlayerCommands.class)
	public void raz(CommandContext args, CommandSource source) throws CommandException {
	}
}
