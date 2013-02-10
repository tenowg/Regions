
package com.thedemgel.regions.data;

import java.util.concurrent.ConcurrentHashMap;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.util.list.concurrent.ConcurrentList;


public class RegionData {
	public static final DefaultedKey<ConcurrentHashMap> FEATURES = new DefaultedKeyImpl<ConcurrentHashMap>("features", new ConcurrentHashMap<String, FeaturePlugin>());
}
