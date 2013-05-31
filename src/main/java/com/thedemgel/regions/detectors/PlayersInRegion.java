
package com.thedemgel.regions.detectors;

import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;


public class PlayersInRegion extends Detector {
	private Set<Player> players = Collections.newSetFromMap(new ConcurrentHashMap<Player, Boolean>());

	@Override
	public void execute() {
		World world = getFeature().getHolder().getRegion().getWorld();
		WorldRegionComponent worldcomp = world.get(WorldRegionComponent.class);
		for (Player player : world.getPlayers()) {
			Set<Region> regions = worldcomp.getRegion(player);
			if (regions.contains(getFeature().getHolder().getRegion())) {
				players.add(player);
			} else {
				if (players.contains(player)) {
					players.remove(player);
				}
			}
		}
	}
	
	public Set<Player> getPlayers() {
		return players;
	}

}
