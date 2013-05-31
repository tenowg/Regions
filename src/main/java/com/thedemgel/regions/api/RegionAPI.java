package com.thedemgel.regions.api;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.component.PlayerRegionComponent;
import com.thedemgel.regions.component.WorldRegionComponent;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.data.UpdatedRegion;
import com.thedemgel.regions.exception.InvalidPointPosition;
import com.thedemgel.regions.exception.RegionNotFoundException;
import com.thedemgel.regions.exception.PointsNotSetException;
import com.thedemgel.regions.exception.RegionAlreadyExistsException;
import com.thedemgel.regions.exception.VolumeTypeNotFoundException;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;

public class RegionAPI {

	public static Region selectRegion(Player player, String regionname) throws RegionNotFoundException {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionname);

		if (region == null) {
			//player.sendMessage(ChatStyle.RED, "No region exists by that name.");
			throw new RegionNotFoundException();
		}

		player.get(PlayerRegionComponent.class).setSelectedRegion(region);
		//player.sendMessage("Region Selected: " + region.getName());
		return region;
	}

	public static Points setPosition(Player player, String enumString) throws InvalidPointPosition {
		PlayerRegionComponent preg = player.get(PlayerRegionComponent.class);

		Points point = preg.setPos(enumString, player.getScene().getPosition());

		if (point == null) {
			//player.sendMessage(ChatStyle.CYAN, "Position ", args.getString(0).toUpperCase()," not Set. (Not an position value)");
			throw new InvalidPointPosition();
		}

		return point;
	}

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

	public static Class<? extends Volume> setSelectedVolumeType(Player player, String volumeString) throws VolumeTypeNotFoundException {
		Class<? extends Volume> volume = Regions.getInstance().getVolume(volumeString);

		if (volume == null) {
			throw new VolumeTypeNotFoundException();
		}

		player.get(PlayerRegionComponent.class).setVolumeType(volume);
		return volume;
	}

	public static void removeRegion(Player player, String regionString) throws RegionNotFoundException {
		Region region = player.getWorld().get(WorldRegionComponent.class).getRegion(regionString);

		if (region != null) {
			player.getWorld().get(WorldRegionComponent.class).removeRegion(region);
		} else {
			throw new RegionNotFoundException();
		}
	}
}
