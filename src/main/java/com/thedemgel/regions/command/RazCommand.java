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

	@Command(aliases = {"region", "raz"}, usage = "", desc = "Access region commands")
	@NestedCommand(PlayerCommands.class)
	public void raz(CommandContext args, CommandSource source) throws CommandException {
	}
}
