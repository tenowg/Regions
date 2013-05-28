package com.thedemgel.regions.component;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.data.UpdatedRegion;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import com.thedemgel.regions.volume.volumes.VolumeBox;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

/**
 * Player Region Component used to control Region Selection.
 */
public class PlayerRegionComponent extends EntityComponent {

	private Region selectedRegion;
	private Class<? extends Volume> volType = VolumeBox.class;
	
	private ConcurrentMap<Points, Vector3> points = new ConcurrentHashMap<>();

	@Override
	public void onAttached() {
	}

	/**
	 * 
	 * @param name The string value of the ENUM to set
	 * @param point The Point location to set
	 * @return The Points (ENUM) that was set - null if not found
	 */
	public Points setPos(String name, Point point) {
		Points pNum = selectedRegion.getVolume().getEnum(name.toUpperCase());

		if (pNum != null) {
			points.put(pNum, point);
		}
		return pNum;
	}

	/**
	 * Returns the selected Region. This will return null if not Region was selected
	 * @return Selected Region or NULL
	 */
	public Region getSelectedRegion() {
		return selectedRegion;
	}

	/**
	 * Will replace the selected region with a new Region file.
	 */
	public void newSelection() {
		selectedRegion = new Region(volType);
	}

	/**
	 * Retrieves a regions and places it in the selectionRegion.
	 * @param region the Region to select
	 */
	public void setSelectedRegion(Region region) {
		this.selectedRegion = region;
		volType = region.getVolume().getClass();
	}

	/**
	 * Will set the Volume type of a region, will also
	 * reset the selectedRegion
	 * TODO: not have to reset the selected region.
	 * @param type 
	 */
	public void setVolumeType(Class<? extends Volume> type) {
		volType = type;
		selectedRegion = new Region(type);
	}

	/**
	 * Will return the current VolumeType to be used by regions.
	 * @return Volume class
	 */
	public Class<? extends Volume> getVolumeType() {
		return volType;
	}

	/**
	 * Updates a selections MIN/MAX
	 * @return Updated Region
	 */
	public UpdatedRegion updateSelected() {
		//selectedRegion.setMinMax(pos1, pos2);
		UpdatedRegion ureg = new UpdatedRegion(selectedRegion);
		
		for (Points pNum : selectedRegion.getVolume().getEnum()) {
			if (!points.containsKey(pNum)) {
				ureg.setUpdated(false);
				ureg.addErr(pNum);
			}
		}
		
		if (!ureg.getUpdated()) {
			return ureg;
		}
		
		for (Entry<Points, Vector3> point : points.entrySet()) {
			selectedRegion.getVolume().setPoint(point.getKey(), point.getValue());
		}

		if(getOwner().getWorld().get(WorldRegionComponent.class).updateRegion(selectedRegion) == null) {
			ureg.setExists(true);
		}
		
		return ureg;
	}

	/**
	 * Will commit a new region to the system. Used after newSelection()
	 * @param name Name of the purposed region
	 * @return UpdatedRegion object
	 */
	public UpdatedRegion createSelected(String name) {
		UpdatedRegion ureg = new UpdatedRegion(selectedRegion);
		
		for (Points pNum : selectedRegion.getVolume().getEnum()) {
			Regions.getInstance().getLogger().info(pNum.toString());
			if (!points.containsKey(pNum)) {
				ureg.setUpdated(false);
				ureg.addErr(pNum);
			}
		}
		
		if (!ureg.getUpdated()) {
			return ureg;
		}
		
		for (Entry<Points, Vector3> point : points.entrySet()) {
			selectedRegion.getVolume().setPoint(point.getKey(), point.getValue());
		}

		if (getOwner().getWorld().get(WorldRegionComponent.class).createRegion((Player) getOwner(), name) == null) {
			ureg.setExists(true);
		}
	
		return ureg;
	}
}
