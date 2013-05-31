package com.thedemgel.regions.api;

import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.exception.NoSuchRegion;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;

public class RegionAPI {

	public static Region selectRegion(Player player, String regionname) throws NoSuchRegion {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionname);

		if (region == null) {
			//player.sendMessage(ChatStyle.RED, "No region exists by that name.");
			throw new NoSuchRegion();
		}

		player.get(PlayerRegionComponent.class).setSelectedRegion(region);
		//player.sendMessage("Region Selected: " + region.getName());
		return region;
	}
}
