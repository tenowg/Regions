
package com.thedemgel.regions.feature.features;

import com.thedemgel.regions.feature.Feature;
import org.spout.api.entity.Player;
import org.spout.api.util.list.concurrent.ConcurrentList;


public class Owner extends Feature {
	private static final long serialVersionUID = 1L;
	private ConcurrentList<String> owners = new ConcurrentList<String>();
	
	public void addOwner(Player player) {
		owners.add(player.getName());
	}
	
	public void removeOwner(Player player) {
		owners.remove(player.getName());
	}
	
	public ConcurrentList<String> getOwners() {
		return owners;
	}
	
	public boolean isOwner(Player player) {
		if(owners.contains(player.getName())) {
			return true;
		}
		
		return false;
	}
}
