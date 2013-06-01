package com.thedemgel.regions.api;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.data.UpdatedRegion;
import com.thedemgel.regions.exception.InvalidPointPosition;
import com.thedemgel.regions.exception.PointsNotSetException;
import com.thedemgel.regions.exception.RegionAlreadyExistsException;
import com.thedemgel.regions.exception.RegionNotFoundException;
import com.thedemgel.regions.exception.VolumeTypeNotFoundException;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.spout.api.entity.Player;

public class RegionAPI {

	/**
	 * Will search for and return a region by String name and place that into
	 * Players selected region slot.
	 * 
	 * @param player
	 * @param regionname
	 * @return Region that was selected
	 * @throws RegionNotFoundException
	 */
	public static Region selectRegion(Player player, String regionname) throws RegionNotFoundException {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionname);
		return selectRegion(player, region);
	}
	
	/**
	 * Will search for and return a region by UUID value and place that into
	 * the Players selected region slot.
	 * 
	 * @param player
	 * @param regionuuid
	 * @return Region that was selected
	 * @throws RegionNotFoundException
	 */
	public static Region selectRegion(Player player, UUID regionuuid) throws RegionNotFoundException {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionuuid);
		return selectRegion(player, region);
	}
	/**
	 * Will place the region passed it no the players Selected Region slot.
	 * 
	 * @param player
	 * @param region
	 * @return Region that was selected
	 * @throws RegionNotFoundException
	 */
	public static Region selectRegion(Player player, Region region) throws RegionNotFoundException {
		if (region == null) {
			//player.sendMessage(ChatStyle.RED, "No region exists by that name.");
			throw new RegionNotFoundException();
		}

		player.get(PlayerRegionComponent.class).setSelectedRegion(region);
		//player.sendMessage("Region Selected: " + region.getName());
		return region;
	}

	/**
	 * Will set and return the Points that was set by this method. Will throw
	 * a InvalidPointPosition Exception if an invalid ENUM or point was not settable.
	 * 
	 * @param player
	 * @param enumString
	 * @return The Points that was set.
	 * @throws InvalidPointPosition
	 */
	public static Points setPosition(Player player, String enumString) throws InvalidPointPosition {
		PlayerRegionComponent preg = player.get(PlayerRegionComponent.class);

		Points point = preg.setPos(enumString, player.getScene().getPosition());

		if (point == null) {
			//player.sendMessage(ChatStyle.CYAN, "Position ", args.getString(0).toUpperCase()," not Set. (Not an position value)");
			throw new InvalidPointPosition();
		}

		return point;
	}

	/**
	 * Will attempt to update a currently loaded region, this will only work
	 * if the region was previously selected and is loaded into the players
	 * selected Region slot. Will throw RegionNotFoundException if region
	 * was not previously loaded, and will throw PointsNotSetException if
	 * all points are not correctly set.
	 * 
	 * @param player
	 * @return The Region Updated.
	 * @throws PointsNotSetException
	 * @throws RegionNotFoundException
	 */
	public static Region updateRegion(Player player) throws PointsNotSetException, RegionNotFoundException {
		UpdatedRegion ureg = player.get(PlayerRegionComponent.class).updateSelected();

		if (!ureg.getUpdated()) {
			Set<Points> points = Collections.newSetFromMap(new ConcurrentHashMap<Points, Boolean>());
			for (Points point : ureg.getErrorPoints()) {
				//player.sendMessage(ChatStyle.RED, point, " is not set. (", point.desc());
				points.add(point);
			}
			//player.sendMessage(ChatStyle.RED, "Failed to Update Region.");
			throw new PointsNotSetException(points);
		}

		if (ureg.getExists()) {
			//player.sendMessage(ChatStyle.RED, "Region already exists, try creating first.");
			throw new RegionNotFoundException();
		}

		return ureg.getRegion();
	}

	/**
	 * This will attempt to create the region that is in the Players selected
	 * region slot. Will return with PointsNotSetException if all points of
	 * the Volume are not set correctly, and will throw RegionAlreadyExistsException
	 * if the Region had previously been created (player will need to use
	 * updateRegion instead)
	 * 
	 * @param player
	 * @return The region created.
	 * @throws PointsNotSetException
	 * @throws RegionAlreadyExistsException
	 */
	public static Region createRegion(Player player) throws PointsNotSetException, RegionAlreadyExistsException {
		UpdatedRegion ureg = player.get(PlayerRegionComponent.class).updateSelected();

		if (!ureg.getUpdated()) {
			Set<Points> points = Collections.newSetFromMap(new ConcurrentHashMap<Points, Boolean>());
			for (Points point : ureg.getErrorPoints()) {
				//player.sendMessage(ChatStyle.RED, point, " is not set. (", point.desc());
				points.add(point);
			}
			//player.sendMessage(ChatStyle.RED, "Failed to Update Region.");
			throw new PointsNotSetException(points);
		}

		if (!ureg.getExists()) {
			//player.sendMessage(ChatStyle.RED, "Region already exists, try creating first.");
			throw new RegionAlreadyExistsException();
		}

		return ureg.getRegion();
	}

	/**
	 * Sets the VolumeType of the Volume in the players Selected Region Slot.
	 * This will reset the bounds of the Region Volume immediately.
	 * 
	 * Will return a VolumeTypeNotFoundException if the Volume type is not
	 * found in the registered Volume Types.
	 * 
	 * @param player
	 * @param volumeString
	 * @return The Class of the Volume to be set
	 * @throws VolumeTypeNotFoundException
	 */
	public static Class<? extends Volume> setSelectedVolumeType(Player player, String volumeString) throws VolumeTypeNotFoundException {
		Class<? extends Volume> volume = Regions.getInstance().getVolume(volumeString);

		if (volume == null) {
			throw new VolumeTypeNotFoundException();
		}

		player.get(PlayerRegionComponent.class).setVolumeType(volume);
		return volume;
	}

	/**
	 * Will attempt to Remove a region by Region String name.
	 * 
	 * Will throw RegionNotFoundException if the Region isn't loaded.
	 * 
	 * @param player
	 * @param regionString
	 * @throws RegionNotFoundException
	 */
	public static void removeRegion(Player player, String regionString) throws RegionNotFoundException {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionString);

		removeRegion(player, region);
	}
	
	/**
	 * Will attempt to Remove a region by Region UUID.
	 * 
	 * Will throw RegionNotFoundException if the Region isn't loaded.
	 * 
	 * @param player
	 * @param regionuuid
	 * @throws RegionNotFoundException
	 */
	public static void removeRegion(Player player, UUID regionuuid) throws RegionNotFoundException {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionuuid);
		
		removeRegion(player, region);
	}
	
	/**
	 * Will attempt to Remove a region by Region.
	 * 
	 * Will throw RegionNotFoundException if the Region isn't loaded.
	 * 
	 * @param player
	 * @param region
	 * @throws RegionNotFoundException
	 */
	public static void removeRegion(Player player, Region region) throws RegionNotFoundException {
		if (region != null) {
			player.getWorld().get(WorldRegionComponent.class).removeRegion(region);
		} else {
			throw new RegionNotFoundException();
		}
	}
}
