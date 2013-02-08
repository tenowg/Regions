package com.thedemgel.regions.component;

import com.thedemgel.regions.data.BBox;
import com.thedemgel.regions.data.Region;
import java.util.Set;
import me.dzineit.selectionapi.SelectionPlayer;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;

/**
 * This file is an example, and will be removed once the Plugin is ready for
 * release.
 *
 * @author tenowg
 */
public class PlayerRegionComponent extends EntityComponent {

	private Region selectedRegion = new Region(new BBox(Vector3.ZERO, Vector3.ZERO));
	private SelectionPlayer playerSel;
	private Vector3 pos1 = null;
	private Vector3 pos2 = null;
	private boolean isBlock1 = false;
	private boolean isBlock2 = false;

	@Override
	public void onAttached() {
		playerSel = getOwner().add(SelectionPlayer.class);
	}

	public void setPos1() {
		setPos1(false);
	}

	public void setPos1(boolean isBlock) {
		isBlock1 = isBlock;
		adjustPos();
	}

	public void setPos2() {
		setPos2(false);
	}

	public void setPos2(boolean isBlock) {
		isBlock2 = isBlock;
		adjustPos();
	}
	
	public Region getSelectedRegion() {
		return selectedRegion;
	}
	
	public void setSelectedRegion(Region region) {
		this.selectedRegion = region;
		pos1 = region.getRegion().getMin();
		pos2 = region.getRegion().getMax();
	}
	
	private void adjustPos() {
		Point a = playerSel.getSelection().getPos1();
		Point b = playerSel.getSelection().getPos2();
		
		if (a == null || b == null) {
			return;
		}
		
		float x = a.getX();
		float y = a.getY();
		float z = a.getZ();
		
		if (isBlock1) {
			if (a.getX() > b.getX()) {
				x = a.getBlockX() + 1;
			} else {
				x = a.getBlockX();
			}
			if (a.getY() > b.getY()) {
				y = a.getBlockY();
			} else {
				y = a.getBlockY() - 1;
			}
			if (a.getZ() > b.getZ()) {
				z = a.getBlockZ() + 1;
			} else {
				z = a.getBlockZ();
			}
		}
		
		pos1 = new Vector3(x, y, z);
		
		x = b.getX();
		y = b.getY();
		z = b.getZ();

		if (isBlock2) {
			if (b.getX() > a.getX()) {
				x = b.getBlockX() + 1;
			} else {
				x = b.getBlockX();
			}
			if (b.getY() > a.getY()) {
				y = b.getBlockY();
			} else {
				y = b.getBlockY() - 1;
			}
			if (b.getZ() > a.getZ()) {
				z = b.getBlockZ() + 1;
			} else {
				z = b.getBlockZ();
			}
		}
		
		pos2 = new Vector3(x, y, z);
	}
	
	public Region updateSelected() {
		selectedRegion.setMinMax(pos1, pos2);
		return getOwner().getWorld().getComponentHolder().get(WorldRegionComponent.class).updateRegion(selectedRegion);
	}
	
	public Region createSelected(String name) {
		selectedRegion.setMinMax(pos1, pos2);
		return getOwner().getWorld().getComponentHolder().get(WorldRegionComponent.class).createRegion((Player)getOwner(), name);
	}
}
