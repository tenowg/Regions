package com.thedemgel.regions.component;

import com.thedemgel.regions.data.Region;
import java.util.Set;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

/**
 * This file is an example, and will be removed once the Plugin is ready for
 * release.
 * @author tenowg
 */
public class PlayerRegionComponent extends EntityComponent {

	private int tickcount = 0;
	private Point lastsafe;

	@Override
	public void onTick(float dt) {
		if (tickcount % 2 == 0) {
			long start = System.nanoTime();
			Player player = (Player) getOwner();
			WorldRegionComponent worldComp = getOwner().getWorld().getComponentHolder().get(WorldRegionComponent.class);

			Set<Region> region = worldComp.getRegion(player.getScene().getPosition());
			
			long end = System.nanoTime();
			
			if (region != null) {
				for (Region reg : region) {
					player.sendMessage("IN " + reg.getName());
				}
			} else {
				//lastsafe = player.getScene().getPosition();
			}

			tickcount = 0;
			//player.sendMessage(end - start);
		}
		tickcount++;
	}
}
