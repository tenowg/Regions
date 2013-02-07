package com.thedemgel.regions.component;

import com.thedemgel.regions.data.BBox;
import com.thedemgel.regions.data.PointMap;
import com.thedemgel.regions.data.Region;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import me.dzineit.selectionapi.SelectionPlayer;
import org.spout.api.Spout;
import org.spout.api.component.type.WorldComponent;
import org.spout.api.entity.Player;
import org.spout.api.event.Event;
import org.spout.api.geo.discrete.Point;
import org.spout.api.util.list.concurrent.ConcurrentList;

public class WorldRegionComponent extends WorldComponent {

	private ConcurrentMap<String, Region> regions = new ConcurrentSkipListMap<String, Region>(String.CASE_INSENSITIVE_ORDER);
	private ConcurrentMap<PointMap, ConcurrentList<Region>> xregions = new ConcurrentHashMap<PointMap, ConcurrentList<Region>>();
	
	public void addRegion(Region region) {
		if (!regions.containsValue(region)) {
			getData().get("regions", new ConcurrentSkipListMap<String, Region>(String.CASE_INSENSITIVE_ORDER)).put(region.getName(), region);
			regions = getData().get("regions", new ConcurrentSkipListMap<String, Region>(String.CASE_INSENSITIVE_ORDER));
		}
		
		int i, ii, iii;

		for (i = (int) (region.getRegion().getLowX() / 100); i <= (int) (region.getRegion().getHighX() / 100); i++) {
			for (ii = (int) (region.getRegion().getLowY() / 100); ii <= (int) (region.getRegion().getHighY() / 100); ii++) {
				for (iii = (int) (region.getRegion().getLowZ() / 100); iii <= (int) (region.getRegion().getHighZ() / 100); iii++) {
					PointMap mpoint = new PointMap(i, ii, iii);
					ConcurrentList<Region> regs = xregions.get(mpoint);
					if (regs == null) {
						regs = new ConcurrentList<Region>();
					}
					regs.add(region);
					xregions.put(mpoint, regs);
				}
			}
		}

		for (Region reg : getData().get("regions", new ConcurrentSkipListMap<String, Region>(String.CASE_INSENSITIVE_ORDER)).values()) {
			Spout.getLogger().info(reg.getRegion().toString());
		}

	}

	/**
	 * Gets a region by name.
	 * @param name
	 * @return 
	 */
	public Region getRegion(String name) {
		return getData().get("regions", new ConcurrentSkipListMap<String, Region>(String.CASE_INSENSITIVE_ORDER)).get(name);
	}

	public Region createRegion(Player player, String name) {
		SelectionPlayer selplayer = player.get(SelectionPlayer.class);
		Spout.getLogger().info(selplayer.toString());
		BBox bos = new BBox(selplayer.getSelection().getPos1(), selplayer.getSelection().getPos2());
		Region region = new Region(bos);
		region.setName(name);
		region.setUUID(UUID.randomUUID());
		addRegion(region);
		return region;
	}

	/**
	 * Will return the Region that contains a specific point.
	 * @param point
	 * @return 
	 */
	public Region inRegion(Point point) {
		PointMap mpoint = new PointMap(point);
		if (xregions.containsKey(mpoint)) {
			for (Region region : xregions.get(mpoint)) {
				if (region.getRegion().containsPoint(point)) {
					return region;
				}
			}
		}

		return null;
	}

	public void init() {
		regions = getData().get("regions", new ConcurrentSkipListMap<String, Region>(String.CASE_INSENSITIVE_ORDER));
		for (Region region : regions.values()) {
			addRegion(region);
		}
	}
	
	/**
	 * Attempts to call Features from all RAZs worldwide
	 * (care needs to be taken when using this)
	 * @param event 
	 */
	public void execute(Event event) {
		for (Region reg : regions.values()) {
			reg.getHolder().execute(event, reg);
		}
	}
	
	/**
	 * Method to call Features from Point (preferred method)
	 * @param event
	 * @param point
	 */
	public void execute(Event event, Point point) {
		Region region = inRegion(point);
		if (region != null) {
			region.getHolder().execute(event, region);
		}
	}
	
	/**
	 * Method to call Features from Region Name
	 * 
	 * Use to call feature execute from other directly.
	 * It is possible to send NULL events.
	 * @param event
	 * @param regionName 
	 */
	public void execute(Event event, String regionName) {
		Region region = getRegion(regionName);
		if (region != null) {
			region.getHolder().execute(event, region);
		}
	}
	
	/**
	 * Will attempt to fire all OnTicks in all Features worldwide.
	 * @param dt 
	 */
	@Override
	public void onTick(float dt) {
		for (Region reg : regions.values()) {
			reg.getHolder().onTick(dt, reg);
		}
	}
}
