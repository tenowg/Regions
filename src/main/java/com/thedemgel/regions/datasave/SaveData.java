package com.thedemgel.regions.datasave;

import com.thedemgel.regions.annotations.Data;
import com.thedemgel.regions.feature.Feature;
import com.thedemgel.regions.volume.Volume;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parse and save Volume information.
 */
public class SaveData {

	private RegionData data;

	public SaveData(RegionData data) {
		this.data = data;
	}

	public void save() {
		Volume volume = data.getRegion().getVolume();
		Class<? extends Volume> clazz = volume.getClass();

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			if (field.isAnnotationPresent(Data.class)) {
				try {
					field.setAccessible(true);
					data.VOLUME.setValue(field.getName(), field.get(volume));
					System.out.println(field.getName() + " -- " + field.get(volume));
				} catch (IllegalArgumentException | IllegalAccessException ex) {
					Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		Collection<Feature> features = data.getRegion().getHolder().getFeatures().values();

		for (Feature feature : features) {
			Class<? extends Feature> featclazz = feature.getClass();

			Field[] featfields = featclazz.getDeclaredFields();

			for (Field featfield : featfields) {
				if (featfield.isAnnotationPresent(Data.class)) {
					try {
						featfield.setAccessible(true);
						//data.VOLUME.setValue(featfield.getName(), featfield.get(volume));
						System.out.println(featfield.getName() + " -- " + featfield.get(volume));
					} catch (IllegalArgumentException | IllegalAccessException ex) {
						Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}
	}
}
