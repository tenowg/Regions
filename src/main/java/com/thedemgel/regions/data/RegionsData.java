
package com.thedemgel.regions.data;

import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;


public final class RegionsData {
	public static final DefaultedKey<ConcurrentSkipListMap> REGIONS = new DefaultedKeyImpl<ConcurrentSkipListMap>("regions", new ConcurrentSkipListMap<UUID, Region>());
}
