package com.thedemgel.regions.util;

import org.yaml.snakeyaml.constructor.Constructor;

public class RegionYamlConstructor extends Constructor {

	public RegionYamlConstructor() {
	}

	@Override
	protected Class<?> getClassForName(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Class not found");
		}
	}
}
