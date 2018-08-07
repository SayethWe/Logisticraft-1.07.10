package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.materials.LogisticraftMaterial;
import com.sinesection.logisticraft.materials.MaterialLogisticraftFuel;

public class ModMaterials {

	public final static MaterialLogisticraftFuel fuel = new MaterialLogisticraftFuel();

	public static final Set<LogisticraftMaterial> materials = new HashSet<>();

	public static final void registerMaterials() {
		/*
		 * for(LogisticraftMaterial mat : materials) {
		 * 
		 * }
		 */
	}

	public static void loadMaterials() {
		loadMaterial(fuel);
	}

	private static void loadMaterial(LogisticraftMaterial i) {
		materials.add(i);
	}
}
