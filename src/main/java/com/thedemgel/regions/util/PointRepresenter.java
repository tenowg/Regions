package com.thedemgel.regions.util;

import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class PointRepresenter extends Representer {

	@Override
	protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
		Object propertyValue, Tag customTag) {
		if ("max".equals(property.getName()) || "min".equals(property.getName())) {
			return null;
		} else {
			return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
		}
	}
}
