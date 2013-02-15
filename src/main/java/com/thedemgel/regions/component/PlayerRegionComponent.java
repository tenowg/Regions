package com.thedemgel.regions.component;

import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.data.UpdatedRegion;
import com.thedemgel.regions.volume.Volume;
import com.thedemgel.regions.volume.points.Points;
import com.thedemgel.regions.volume.volumes.VolumeBox;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.dzineit.selectionapi.SelectionPlayer;
import org.spout.api.Spout;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

/**
 * Player Region Component used to control Region Selection.
 */
public class PlayerRegionComponent extends EntityComponent {

	private Region selectedRegion;
	private SelectionPlayer playerSel;
	private Vector3 pos1 = null;
	private Vector3 pos2 = null;
	private boolean isBlock1 = false;
	private boolean isBlock2 = false;
	private Class<? extends Volume> volType = VolumeBox.class;
	
	private ConcurrentMap<Points, Vector3> points = new ConcurrentHashMap<Points, Vector3>();

	@Override
	public void onAttached() {
		playerSel = getOwner().add(SelectionPlayer.class);
	}

	public Points setPos(String name, Point point) {
		Points pNum = selectedRegion.getVolume().getEnum(name.toUpperCase());

		if (pNum != null) {
			points.put(pNum, point);
		}
		return pNum;
	}

	public Region getSelectedRegion() {
		return selectedRegion;
	}

	public void newSelection() {
		selectedRegion = new Region(volType);
	}

	public void setSelectedRegion(Region region) {
		this.selectedRegion = region;
		pos1 = region.getVolume().getMin();
		pos2 = region.getVolume().getMax();
		volType = region.getVolume().getClass();
	}

	public void setVolumeType(Class<? extends Volume> type) {
		volType = type;
		selectedRegion = new Region(type);
	}

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

		if(getOwner().getWorld().getComponentHolder().get(WorldRegionComponent.class).updateRegion(selectedRegion) == null) {
			ureg.setExists(true);
		}
		
		return ureg;
	}

	public UpdatedRegion createSelected(String name) {
		UpdatedRegion ureg = new UpdatedRegion(selectedRegion);
		
		for (Points pNum : selectedRegion.getVolume().getEnum()) {
			Spout.getLogger().info(pNum.toString());
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

		if (getOwner().getWorld().getComponentHolder().get(WorldRegionComponent.class).createRegion((Player) getOwner(), name) == null) {
			ureg.setExists(true);
		}
	
		return ureg;
	}
}
