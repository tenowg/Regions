package com.thedemgel.regions.feature;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.data.EventRegion;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.util.RegionYamlConstructor;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.Plugin;
import org.spout.api.util.list.concurrent.ConcurrentList;
import org.yaml.snakeyaml.Yaml;

/**
 * FeatureHolders contain all attached Features that regions execute either
 * onTick or by passed in Event from WorldRegionComponent.
 *
 * parentFeatures is transient due to the off chance that java creates many new
 * objects instead of deserializing as it should, so all parent features will
 * need to be manually saved and reinitialized on startup.
 *
 * @author tenowg
 */
public class FeatureHolder implements Serializable {

	private static final long serialVersionUID = 56L;
	transient private ConcurrentMap<String, Class<? extends Feature>> featureNames = new ConcurrentSkipListMap<String, Class<? extends Feature>>(String.CASE_INSENSITIVE_ORDER);
	transient private ConcurrentMap<Class<? extends Feature>, Feature> features = new ConcurrentHashMap<Class<? extends Feature>, Feature>();
	transient private ConcurrentList<ParentFeatureHolder> parentFeatures = new ConcurrentList<ParentFeatureHolder>();
	private List<String> yamls = new ArrayList<String>();

	public FeatureHolder() {
	}

	/**
	 * Add a Feature to this FeatureHolder
	 *
	 * @param clazz
	 * @return Either the Feature added, Previous Feature, or null if
	 * failure.
	 */
	public <T extends Feature> T add(Plugin plugin, Class<T> clazz) {

		Feature feature = null;
		try {
			feature = clazz.newInstance();
		} catch (InstantiationException ex) {
			Spout.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
		} catch (IllegalAccessException ex) {
			Spout.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
		}

		if (feature == null) {
			return null;
		}

		if (feature.attachTo(plugin, this)) {
			if (!features.containsKey(clazz)) {
				features.put(clazz, feature);
				featureNames.put(feature.getClass().getSimpleName(), feature.getClass());

				feature.onAttached();
			}
		} else {
			return null;
		}

		return (T) feature;
	}

	/**
	 * Retrieve a Feature that is attached to this FeatureHolder
	 *
	 * @param clazz
	 * @return Feature if one if found, otherwise null
	 */
	public <T extends Feature> T get(Class<T> clazz) {
		if (features.containsKey(clazz)) {
			return (T) features.get(clazz);
		}
		return null;
	}

	/**
	 * Retrieve a Feature that is attached to this FeatureHolder, BY NAME.
	 * @param <T>
	 * @param name String value to search
	 * @return Feature if one if found, otherwise null
	 */
	public <T extends Feature> T get(String name) {
		Class<? extends Feature> feat = featureNames.get(name);
		if (feat != null) {
			return (T) features.get(feat);
		}
		return null;
	}
	/**
	 * Remove a Feature that is attached to this FeatureHolder
	 *
	 * @param clazz Class to remove
	 */
	public <T extends Feature> void detach(Class<T> clazz) {
		features.get(clazz).onDetached();
		features.remove(clazz);
		featureNames.remove(clazz.getSimpleName());
	}

	/**
	 * Add a parent FeatureHolder to holder.
	 *
	 * @param holder FeatureHolder to add.
	 */
	public void addFeatureHolder(ParentFeatureHolder holder) {
		parentFeatures.add(holder);
	}

	/**
	 * Remove a Parent FeatureHolder from this FeatureHolder.
	 *
	 * @param holder FeatureHolder to remove.
	 */
	public void removeFeatureHolder(ParentFeatureHolder holder) {
		parentFeatures.remove(holder);
	}

	/**
	 * Executes all execute methods while passing EVENT to all Features.
	 *
	 * @param event Event passed to Features to be executed.
	 */

	public void execute(Event event, EventRegion regions) {
		for (FeatureHolder parent : parentFeatures) {
			//parent.execute(event, region);
			for (Entry<Class<? extends Feature>, Feature> feature : parent.features.entrySet()) {
				if (!features.containsKey(feature.getKey())) {
					feature.getValue().execute(event, regions);
				}
			}
		}

		for (Feature feature : features.values()) {
			feature.execute(event, regions);
		}
	}

	public void onTick(float dt, Region region) {
		for (FeatureHolder parent : parentFeatures) {
			parent.onTick(dt, region);
		}

		for (Feature feature : features.values()) {
			feature.tick(dt, region);
		}
	}

	public ConcurrentMap<Class<? extends Feature>, Feature> getFeatures() {
		return features;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		Yaml beanWriter = new Yaml();

		if (yamls == null) {
			yamls = new ConcurrentList<String>();
		}
		yamls.clear();
		for (Feature feat : features.values()) {
			yamls.add(beanWriter.dump(feat));
		}
		
		for (String yam : yamls) {
			Spout.getLogger().info(yam);
		}
		oos.defaultWriteObject();
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		parentFeatures = new ConcurrentList<ParentFeatureHolder>();
		features = new ConcurrentHashMap<Class<? extends Feature>, Feature>();
		featureNames = new ConcurrentSkipListMap<String, Class<? extends Feature>>(String.CASE_INSENSITIVE_ORDER);

		Yaml loader = new Yaml(new RegionYamlConstructor());

		if (yamls != null) {
			for (String yam : yamls) {
				Feature feat;
				try {
					feat = (Feature) loader.load(yam);
				} catch (Exception e) {
					Spout.getLogger().warning("Feature failed load or was not found: " + yam);
					Spout.getLogger().info("Attempting to find replacement.");
					// Do class lookup and try to reload from scratch.]
					String string = yam.substring(0, yam.indexOf(" "));
					string = string.substring(string.lastIndexOf(".") + 1);
					String plug = yam.substring(yam.indexOf("pluginName") + 12);
					if (plug.contains(",")) {
						plug = plug.substring(0, plug.indexOf(","));
					} else if (plug.contains("}")) {
						plug = plug.substring(0, plug.indexOf("}"));
					}
					Plugin lookupPlugin = Spout.getPluginManager().getPlugin(plug);
					Class featureClazz = Regions.getInstance().getFeature((CommonPlugin) lookupPlugin, string);
					add(lookupPlugin, featureClazz);
					continue;
				}
				//Spout.getLogger().info(feat.toString());
				Plugin plug = Spout.getPluginManager().getPlugin(feat.getPluginName());
				if (feat.attachTo(plug, this)) {
					features.put(feat.getClass(), feat);
					featureNames.put(feat.getClass().getSimpleName(), feat.getClass());
				}
			}
		}
	}
}
