package com.thedemgel.regions.api;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.api.event.RemoveRegionEvent;
import com.thedemgel.regions.api.event.SelectRegionEvent;
import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.data.UpdatedRegion;
import com.thedemgel.regions.exception.InvalidPointPositionException;
import com.thedemgel.regions.exception.PointsNotSetException;
import com.thedemgel.regions.exception.RegionAlreadyExistsException;
import com.thedemgel.regions.exception.RegionNotFoundException;
import com.thedemgel.regions.exception.SelectionCancelledException;
import com.thedemgel.regions.exception.VolumeTypeNotFoundException;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.parser.EventParser;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.plugin.Plugin;

/**
 * The general API to be used to access Regions from other plugins. Most functions should be available
 * through this API, if for some reason a function is not available, please let a developer know
 * and a solution will likely be added.
 */
public final class RegionAPI {

	private RegionAPI() { }

	/**
	 * Register your feature with Regions, this process will automatically register
	 * any Event listed with Spout to be fired in the WorldRegionComponent and
	 * thus be pasted to the Features attached to Regions.
	 *
	 * This method will use the Default EventParser.
	 *
	 * @param plugin Plugin that is registering the feature.
	 * @param feature Feature being Registered
	 */
	public static void registerFeature(Plugin plugin, Class<? extends Feature> feature) {
		Regions.getInstance().getFeatureRegister().registerFeature(plugin, feature);
	}

	/**
	 * Register your feature with Regions, this process will automatically register
	 * any Event listed with Spout to be fired in the WorldRegionComponent and
	 * thus be pasted to the Features attached to Regions.
	 *
	 * This method will use the EventParser that is pasted to it.
	 *
	 * @param plugin Plugin that is registering the feature.
	 * @param feature Featuring being registered.
	 * @param parser Custom EventParsering being used in registering this feature.
	 */
	public static void registerFeature(Plugin plugin, Class<? extends Feature> feature, EventParser parser) {
		Regions.getInstance().getFeatureRegister().registerFeature(plugin, feature, parser);
	}

	/**
	 * Will search for and return a region by String name and place that into
	 * Players selected region slot.
	 *
	 * @param player Player selecting the region.
	 * @param regionname The String name of the region being selected.
	 * @return Region that was selected
	 * @throws RegionNotFoundException Exception thrown if String region name is not found.
	 * @throws SelectionCancelledException Thrown when Event fired by select region is canceled.
	 */
	public static Region selectRegion(Player player, String regionname, Plugin plugin) throws RegionNotFoundException, SelectionCancelledException {		
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionname);

		return selectRegion(player, region, plugin);
	}

	/**
	 * Will search for and return a region by UUID value and place that into
	 * the Players selected region slot.
	 *
	 * @param player Player attempting to select a region.
	 * @param regionuuid The UUID of the region to be selected.
	 * @return Region that was selected
	 * @throws RegionNotFoundException Thrown if UUID is not found in loaded regions.
	 * @throws SelectionCancelledException Thrown when Event fired by select region is canceled.
	 */
	public static Region selectRegion(Player player, UUID regionuuid, Plugin plugin) throws RegionNotFoundException, SelectionCancelledException {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionuuid);

		return selectRegion(player, region, plugin);
	}

	/**
	 * Will place the region passed it no the players Selected Region slot.
	 *
	 * @param player Player attempting to select a region.
	 * @param region The Region Object to be selected.
	 * @return Region that was selected
	 * @throws RegionNotFoundException Thrown if Region Object is not found in loaded regions.
	 * @throws SelectionCancelledException Thrown when Event fired by select region is canceled.
	 */
	public static Region selectRegion(Player player, Region region, Plugin plugin) throws RegionNotFoundException, SelectionCancelledException {
		if (region == null) {
			throw new RegionNotFoundException("Region is Null");
		}

		SelectRegionEvent event = new SelectRegionEvent(player, region, plugin);

		Spout.getEventManager().callEvent(event);

		if (event.isCancelled()) {
			throw new SelectionCancelledException("Selection was Cancelled, either another plugin or permissions is preventing selection.");
		}

		player.get(PlayerRegionComponent.class).setSelectedRegion(region);
		return region;
	}

	/**
	 * Will set and return the Points that was set by this method. Will throw
	 * a InvalidPointPositionException Exception if an invalid ENUM or point was not settable.
	 *
	 * @param player Player attempting to set a Position.
	 * @param enumString The String value of the Position attempting to be set.
	 * @return The Points that was set.
	 * @throws InvalidPointPositionException Thrown if enumString is not a valid Position argument.
	 */
	public static Points setPosition(Player player, String enumString) throws InvalidPointPositionException {
		PlayerRegionComponent preg = player.get(PlayerRegionComponent.class);

		Points point = preg.setPos(enumString, player.getPhysics().getPosition());

		if (point == null) {
			//player.sendMessage(ChatStyle.CYAN, "Position ", args.getString(0).toUpperCase()," not Set. (Not an position value)");
			throw new InvalidPointPositionException("Not a proper POS was passed to setPosition.");
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
	 * @param player The player updating their selected region.
	 * @return The Region Updated.
	 * @throws PointsNotSetException Thrown if all Points are not set.
	 * @throws RegionNotFoundException Thrown if region is a New region.
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
			throw new PointsNotSetException(points, "These positions need to be set.");
		}

		if (ureg.getExists()) {
			//player.sendMessage(ChatStyle.RED, "Region already exists, try creating first.");
			throw new RegionNotFoundException("No region was found.");
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
	 * @param player The player attempting to create the region.
	 * @return The region created.
	 * @throws PointsNotSetException Thrown if all points are not set.
	 * @throws RegionAlreadyExistsException Region is not a new Region, update instead.
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
			throw new PointsNotSetException(points, "Some positions were not set.");
		}

		if (!ureg.getExists()) {
			//player.sendMessage(ChatStyle.RED, "Region already exists, try creating first.");
			throw new RegionAlreadyExistsException("Region already exists, try creating region first");
		}

		return ureg.getRegion();
	}

	public static void newRegion(Player player) {
		PlayerRegionComponent regComp = player.get(PlayerRegionComponent.class);
		regComp.newSelection();
	}

	/**
	 * Sets the VolumeType of the Volume in the players Selected Region Slot.
	 * This will reset the bounds of the Region Volume immediately.
	 *
	 * Will return a VolumeTypeNotFoundException if the Volume type is not
	 * found in the registered Volume Types.
	 *
	 * @param player The player attempting to set VolumeType.
	 * @param volumeString String name of the Volume being set.
	 * @return The Class of the Volume to be set
	 * @throws VolumeTypeNotFoundException Thrown when Volume type is not found.
	 */
	public static Class<? extends Volume> setSelectedVolumeType(Player player, String volumeString) throws VolumeTypeNotFoundException {
		Class<? extends Volume> volume = Regions.getInstance().getVolume(volumeString);

		if (volume == null) {
			throw new VolumeTypeNotFoundException("Invalid Volume type (" + volumeString + ")");
		}

		player.get(PlayerRegionComponent.class).setVolumeType(volume);
		return volume;
	}

	/**
	 * Will attempt to Remove a region by Region String name.
	 *
	 * Will throw RegionNotFoundException if the Region isn't loaded.
	 *
	 * @param player Player attempting to remove a region.
	 * @param regionString The String name of the region to be removed.
	 * @throws RegionNotFoundException Thrown when String region name is not found.
	 */
	public static void removeRegion(Player player, String regionString, Plugin plugin) throws RegionNotFoundException {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionString);

		removeRegion(player, region, plugin);
	}

	/**
	 * Will attempt to Remove a region by Region UUID.
	 *
	 * Will throw RegionNotFoundException if the Region isn't loaded.
	 *
	 * @param player Player attempting to remove a region.
	 * @param regionuuid UUID of the region being removed.
	 * @throws RegionNotFoundException Thrown if UUID Region is not found.
	 */
	public static void removeRegion(Player player, UUID regionuuid, Plugin plugin) throws RegionNotFoundException {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionuuid);
		
		removeRegion(player, region, plugin);
	}

	/**
	 * Will attempt to Remove a region by Region.
	 *
	 * Will throw RegionNotFoundException if the Region isn't loaded.
	 *
	 * @param player Player attempting to remove a region.
	 * @param region The Region Object to be removed.
	 * @throws RegionNotFoundException Thrown if Region Object is not found.
	 */
	public static void removeRegion(Player player, Region region, Plugin plugin) throws RegionNotFoundException {
		if (region != null) {
			if(!Spout.getEventManager().callEvent(new RemoveRegionEvent(player, region, plugin)).hasBeenCalled()) {
				player.getWorld().get(WorldRegionComponent.class).removeRegion(region);
			}
		} else {
			throw new RegionNotFoundException("No matching region was found.");
		}
	}

	public static void featureCommand() { }
}
