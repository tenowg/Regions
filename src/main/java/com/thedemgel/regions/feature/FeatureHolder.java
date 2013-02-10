package com.thedemgel.regions.feature;

import com.thedemgel.regions.Regions;
import com.thedemgel.regions.data.BBox;
import com.thedemgel.regions.data.EventRegion;
import com.thedemgel.regions.data.FeaturePlugin;
import com.thedemgel.regions.data.PointMap;
import com.thedemgel.regions.data.Region;
import com.thedemgel.regions.data.RegionData;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.Spout;
import org.spout.api.component.impl.DatatableComponent;
import org.spout.api.datatable.ManagedHashMap;
import org.spout.api.datatable.SerializableMap;
import org.spout.api.event.Event;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.Plugin;
import org.spout.api.util.list.concurrent.ConcurrentList;

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
	transient private DatatableComponent data;
	private byte[] dataMap;
	transient private ConcurrentMap<Class<? extends Feature>, Feature> features = new ConcurrentHashMap<Class<? extends Feature>, Feature>();
	transient private ConcurrentList<FeatureHolder> parentFeatures = new ConcurrentList<FeatureHolder>();

	public FeatureHolder() {
		data = new DatatableComponent();
	}

	public DatatableComponent getData() {
		return data;
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
			Logger.getLogger(Region.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(Region.class.getName()).log(Level.SEVERE, null, ex);
		}

		if (feature == null) {
			return null;
		}

		if (feature.attachTo(plugin, this)) {
			if (!features.containsKey(clazz)) {
				features.put(clazz, feature);
				if (!getData().get(RegionData.FEATURES).contains(plugin.getName())) {
					getData().get(RegionData.FEATURES).put(plugin.getName(), new FeaturePlugin(plugin.getName()));
				}
				if (!((FeaturePlugin) getData().get(RegionData.FEATURES).get(plugin.getName())).getFeatures().contains(feature.getClass().getSimpleName())) {
					((FeaturePlugin) getData().get(RegionData.FEATURES).get(plugin.getName())).getFeatures().add(feature.getClass().getSimpleName());
				}

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
	 * Remove a Feature that is attached to this FeatureHolder
	 *
	 * @param clazz Class to remove
	 */
	public <T extends Feature> void detach(Class<T> clazz) {
		features.get(clazz).onDetached();
		features.remove(clazz);
	}

	/**
	 * Add a parent FeatureHolder to holder.
	 *
	 * @param holder FeatureHolder to add.
	 */
	public void addFeatureHolder(FeatureHolder holder) {
		parentFeatures.add(holder);
	}

	/**
	 * Remove a Parent FeatureHolder from this FeatureHolder.
	 *
	 * @param holder FeatureHolder to remove.
	 */
	public void removeFeatureHolder(FeatureHolder holder) {
		parentFeatures.remove(holder);
	}

	/**
	 * Executes all execute methods while passing EVENT to all Features.
	 *
	 * @param event Event passed to Features to be executed.
	 */
	public void execute(Event event, Region region) {
		for (FeatureHolder parent : parentFeatures) {
			//parent.execute(event, region);
			for (Entry<Class<? extends Feature>, Feature> feature : parent.features.entrySet()) {
				if (!features.containsKey(feature.getKey())) {
					feature.getValue().execute(event, region);
				}
			}
		}

		for (Feature feature : features.values()) {
			feature.execute(event, region);
		}
	}

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
		dataMap = data.serialize();
		oos.defaultWriteObject();
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		parentFeatures = new ConcurrentList<FeatureHolder>();
		data = new DatatableComponent(dataMap);
		features = new ConcurrentHashMap<Class<? extends Feature>, Feature>();
		ConcurrentHashMap<String, FeaturePlugin> oldFeats = new ConcurrentHashMap((ConcurrentHashMap) getData().get(RegionData.FEATURES));
		for (FeaturePlugin feature : oldFeats.values()) {
			for (String feat : feature.getFeatures()) {
				Plugin plug = Spout.getPluginManager().getPlugin(feature.getPlugin());
				Class<? extends Feature> addFeat = Regions.getInstance().getFeature((CommonPlugin) plug, feat);
				if (addFeat != null) {
					add(plug, addFeat);
					Spout.getLogger().info("Adding: " + addFeat.toString());
				}
			}
		}
	}
}
