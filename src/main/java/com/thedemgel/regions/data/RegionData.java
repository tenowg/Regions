
package com.thedemgel.regions.data;

import com.thedemgel.regions.feature.Feature;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.util.list.concurrent.ConcurrentList;


public class RegionData {
	public static final DefaultedKey<ConcurrentList> FEATURES = new DefaultedKeyImpl<ConcurrentList>("features", new ConcurrentList<Class<? extends Feature>>());
}
