package com.thedemgel.regions.component;

import com.thedemgel.regions.data.Region;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;

public class PlayerRegionComponent extends EntityComponent {

	private int tickcount = 0;
	private Point lastsafe;

	@Override
	public void onTick(float dt) {
		if (tickcount % 2 == 0) {
			long start = System.nanoTime();
			Player player = (Player) getOwner();
			WorldRegionComponent worldComp = getOwner().getWorld().getComponentHolder().get(WorldRegionComponent.class);

			Region region = worldComp.inRegion(player.getScene().getPosition());
			
			long end = System.nanoTime();
			
			if (region != null) {
				//player.sendMessage("IN " + region.getName());
			} else {
				//lastsafe = player.getScene().getPosition();
			}

			tickcount = 0;
			//player.sendMessage(end - start);
		}
		tickcount++;
	}
}
