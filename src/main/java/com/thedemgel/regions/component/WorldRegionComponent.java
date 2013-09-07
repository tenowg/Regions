package com.thedemgel.regions.component;

import com.thedemgel.regions.data.EventRegion;
import com.thedemgel.regions.data.PointMap;
import com.thedemgel.regions.data.Region;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.spout.api.component.world.WorldComponent;
import org.spout.api.entity.Player;
import org.spout.api.event.Event;
import org.spout.api.geo.discrete.Point;

/**
 * WorldRegionComponent is where all the regions are stored, and 99% of all
 * Regions are accessed and modified.
 *
 * Should be called with
 * world.getComponentHolder().get(WorldRegionComponent.class) there should be no
 * need to access a region directly, if something is missing and you find you
 * need to access a region, feature, or feature parent directly, please notify
 * me and I will fix it.
 *
 * @author tenowg
 */
public class WorldRegionComponent extends WorldComponent {
	public static final Integer WORLD_GRID_SIZE = 100;

	private ConcurrentMap<UUID, Region> regions = new ConcurrentSkipListMap<>();
	private ConcurrentMap<PointMap, Set<Region>> xregions = new ConcurrentHashMap<>();
	private Executor executor = Executors.newSingleThreadExecutor();

	/**
	 * Will add an already created Region to the Maps and parse its
	 * PointMaps and effectively add the region to the Map. (Should only be
	 * called from createRegion, or if the Region Object has been FULLY
	 * created first)
	 *
	 * @param region
	 */
	public void addRegion(Region region) {
		if (!regions.containsValue(region)) {
			getData().get("regions", new ConcurrentSkipListMap<UUID, Region>()).put(region.getUUID(), region);
			regions = getData().get("regions", new ConcurrentSkipListMap<UUID, Region>());
		}

		updateRegion(region);
	}

	/**
	 * Will completely remove a region from all Maps effectively removing it
	 * from the game.
	 *
	 * @param region Region to be removed
	 */
	public void removeRegion(Region region) {
		for (PointMap mpoint : region.getPointCache()) {
			Set<Region> regs = xregions.get(mpoint);
			regs.remove(region);
		}

		getData().get("regions", new ConcurrentSkipListMap<UUID, Region>()).remove(region.getUUID());
		regions = getData().get("regions", new ConcurrentSkipListMap<UUID, Region>());
	}

	/**
	 * Should be called after the size of a Region has been changed. This
	 * will "fix" the PointMaps and regenerate the PointMap cache of the
	 * region.
	 *
	 * @param region Region after it has been updated with new Coords.
	 */
	public Region updateRegion(Region region) {

		if (region.getUUID() == null) {
			return null;
		}

		for (PointMap mpoint : region.getPointCache()) {
			Set<Region> regs = xregions.get(mpoint);
			if (regs != null) {
				regs.remove(region);
			}
		}

		region.resetPointCache();

		int i, ii, iii;

		for (i = (int) (region.getVolume().getLowX() / WorldRegionComponent.WORLD_GRID_SIZE); i <= (int) (region.getVolume().getHighX() / WorldRegionComponent.WORLD_GRID_SIZE); i++) {
			for (ii = (int) (region.getVolume().getLowY() / WorldRegionComponent.WORLD_GRID_SIZE); ii <= (int) (region.getVolume().getHighY() / WorldRegionComponent.WORLD_GRID_SIZE); ii++) {
				for (iii = (int) (region.getVolume().getLowZ() / WorldRegionComponent.WORLD_GRID_SIZE); iii <= (int) (region.getVolume().getHighZ() / WorldRegionComponent.WORLD_GRID_SIZE); iii++) {
					PointMap mpoint = new PointMap(i, ii, iii);
					Set<Region> regs = xregions.get(mpoint);
					if (regs == null) {
						regs = Collections.newSetFromMap(new ConcurrentHashMap<Region, Boolean>());
					}
					region.addPointCache(mpoint);
					regs.add(region);
					xregions.put(mpoint, regs);
				}
			}
		}

		return region;
	}

	/**
	 * Gets a region by name. Not the preferred method of retrieving a
	 * region. As names can change, and more processor is needed.
	 *
	 * @param name
	 * @return Region whose name is name
	 */
	public Region getRegion(String name) {
		for (Region region : regions.values()) {
			if (region.getName().equalsIgnoreCase(name)) {
				return region;
			}
		}

		return null;
	}

	/**
	 * Gets a region by UUID. Developers should store UUIDs instead of Names
	 * in their plugins As they should never change once the regions are
	 * created, and use less time to search.
	 *
	 * @param uuid UUID of the region being searched.
	 * @return Region whose UUID is uuid
	 */
	public Region getRegion(UUID uuid) {
		return regions.get(uuid);
	}

	/**
	 * Gets a regions a Player is in.
	 *
	 * @param player The player to check
	 * @return Regions the player is currently in
	 */
	public Set<Region> getRegion(Player player) {
		return getRegion(player.getPhysics().getPosition());
	}

	/**
	 * Creates a Region from SelectPlayer selections.
	 *
	 * @param player The player creating the Region.
	 * @param name Name of the Region
	 * @return The Created Region
	 */
	public Region createRegion(Player player, String name) {
		Region region = player.get(PlayerRegionComponent.class).getSelectedRegion();
		if (region.getUUID() != null) {
			return null;
		}

		if (getRegion(name) != null) {
			return null;
		}

		region.setName(name);
		region.setUUID(UUID.randomUUID());
		region.setWorld(player.getWorld().getName());
		addRegion(region);
		return region;
	}

	/**
	 * Will return the Regions that contains a specific point. The return is
	 * unmodifiable.
	 *
	 * @param point
	 * @return Unmodifiable Set of Regions that contain the point
	 */
	public Set<Region> getRegion(Point point) {
		PointMap mpoint = new PointMap(point);
		Set<Region> regionsRet = new HashSet<>();
		if (xregions.containsKey(mpoint)) {
			for (Region region : xregions.get(mpoint)) {
				if (region.getVolume().containsPoint(point)) {
					regionsRet.add(region);
				}

				// NEW CODE
				if (region.getVolume().containsPoint(point)) {
					regionsRet.add(region);
				}
			}
		}

		return Collections.unmodifiableSet(regionsRet);
	}

	/**
	 * The complete set of Regions for this World
	 *
	 * @return ConcurrentMap regions
	 */
	public ConcurrentMap<UUID, Region> getRegions() {
		return regions;
	}

	/**
	 * Lets get this all setup.
	 */
	public void init() {
		regions = getData().get("regions", new ConcurrentSkipListMap<UUID, Region>());
		for (Region region : regions.values()) {
			addRegion(region);
		}
	}

	/**
	 * Attempts to call Features from all RAZs worldwide (care needs to be
	 * taken when using this)
	 *
	 * @param event
	 */
	public void execute(Event event) {
		for (Region reg : regions.values()) {
			EventRegion regs = new EventRegion(null, reg);
			reg.getHolder().execute(event, regs);
		}
	}

	/**
	 * Method to call Features from a Point in a Region.
	 *
	 * Use to call feature execute from other directly. It is possible to
	 * send NULL events.
	 *
	 * @param event
	 * @param point
	 */
	public void execute(Event event, Point point) {
		for (Region region : getRegion(point)) {
			if (region != null) {
				EventRegion eventRegion = new EventRegion(getRegion(point), region);
				region.getHolder().execute(event, eventRegion);
			}
		}
	}

	/**
	 * Method to call Features from Region Name
	 *
	 * Use to call feature execute from other directly. It is possible to
	 * send NULL events. (Not recommended)
	 *
	 * @param event Event to be called
	 * @param regionName Name of the region
	 */
	public void execute(Event event, String regionName) {
		Region region = getRegion(regionName);
		if (region != null) {
			region.getHolder().execute(event, new EventRegion(null, region));
		}
	}

	/**
	 * Method to call Features from a Region's UUID
	 *
	 * Use to call feature execute from other directly. It is possible to
	 * send NULL events. (Preferred method)
	 *
	 * @param event Event to be called
	 * @param uuid UUID of the Region
	 */
	public void execute(Event event, UUID uuid) {
		Region region = getRegion(uuid);
		if (region != null) {
			region.getHolder().execute(event, new EventRegion(null, region));
		}
	}

	/**
	 * Method to call Features from a Region with Region OBJECT.
	 *
	 * Use to call feature execute from other directly. It is possible to
	 * send NULL events. (Fastest way if you have the region object)
	 *
	 * @param event Event to be called
	 * @param region The Region OBJECT
	 */
	public void execute(Event event, Region region) {
		if (region != null) {
			region.getHolder().execute(event, new EventRegion(null, region));
		}
	}

	/**
	 * Will attempt to fire all OnTicks in all Features worldwide.
	 *
	 * @param dt
	 */
	@Override
	public void onTick(float dt) {
		TickRunnable ontick = new TickRunnable(dt);
		executor.execute(ontick);
	}

	private class TickRunnable implements Runnable {

		private final float dt;

		public TickRunnable(float dt) {
			this.dt = dt;
		}

		@Override
		public void run() {
			for (Region reg : regions.values()) {
				reg.getHolder().onTick(dt, reg);
			}
		}
	}
}
