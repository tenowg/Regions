package com.thedemgel.regions.command;

import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.exception.FeatureNotFoundException;
import com.thedemgel.regions.feature.Feature;
import org.spout.api.command.CommandArguments;
import org.spout.api.exception.ArgumentParseException;

public final class RegionArgumentTypes {

	private RegionArgumentTypes() {
	}

	public static Feature popFeature(String argName, CommandArguments args, Region region) throws ArgumentParseException {
		String arg = args.currentArgument(argName);
		Feature feature;
		try {
			feature = region.getHolder().get(arg);
		} catch (FeatureNotFoundException ex) {
			throw args.failure(argName, ex.getMessage(), false);
		}

		return args.success(argName, feature);
	}
}
