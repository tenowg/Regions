package com.thedemgel.regions.command;

import com.thedemgel.regions.Regions;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.CommandDescription;
import org.spout.api.command.annotated.Permissible;
import org.spout.api.exception.CommandException;

public class RazCommand {

	private final Regions plugin;

	public RazCommand(Regions instance) {
		this.plugin = instance;
	}

	@CommandDescription(aliases = {"region", "raz"}, usage = "", desc = "Access region commands")
	@Permissible("regions.command.region")
	public void raz(CommandSource source, CommandArguments args) throws CommandException {
		source.sendMessage("Do Regions help here.");
	}
}
