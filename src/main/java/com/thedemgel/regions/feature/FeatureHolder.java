package com.thedemgel.regions.feature;

import com.thedemgel.regions.data.EventRegion;
import com.thedemgel.regions.data.Region;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.api.Spout;
import org.spout.api.component.impl.DatatableComponent;
import org.spout.api.event.Event;
import org.spout.api.plugin.Plugin;
import org.spout.api.util.list.concurrent.ConcurrentList;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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
	transient private ConcurrentList<ParentFeatureHolder> parentFeatures = new ConcurrentList<ParentFeatureHolder>();
	private List<String> yamls = new ArrayList<String>();

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
		data = new DatatableComponent(dataMap);
		features = new ConcurrentHashMap<Class<? extends Feature>, Feature>();

		Yaml loader = new Yaml(new FilterConstructor(true));

		if (yamls != null) {
			for (String yam : yamls) {
				Feature feat;
				try {
					feat = (Feature) loader.load(yam);
				} catch (Exception e) {
					Spout.getLogger().warning("Feature failed load or was not found: " + yam);
					continue;
				}
				//Spout.getLogger().info(feat.toString());
				Plugin plug = Spout.getPluginManager().getPlugin(feat.getPluginName());
				if (feat.attachTo(plug, this)) {
					features.put(feat.getClass(), feat);
					Spout.getLogger().info("Adding: " + feat.toString());
				}
			}
		}
	}

	class FilterConstructor extends Constructor {

		private boolean filter;

		public FilterConstructor(boolean f) {
			filter = f;
		}

		@Override
		protected Class<?> getClassForName(String name) {
			if (filter && name.contains("FeatureHolder")) {
				throw new RuntimeException("Filter is applied.");
			}
			
			try {
				return Class.forName(name);
			} catch (ClassNotFoundException ex) {
				throw new RuntimeException("Class not found");
			}
		}
	}
}
