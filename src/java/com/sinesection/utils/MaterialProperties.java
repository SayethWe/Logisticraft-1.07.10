package com.sinesection.utils;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.material.Material;
/**
 * all values to three sigfigs
 * @author geekman9097
 *
 */
public class MaterialProperties {

	/**
	 * the amount of energy in joules it takes to raise one mole by one degree kelvin
	 */
	private static final Map<Material,Float> specificHeats = specificHeats();
	/**
	 * the amount one mole of the substance weighs, in grams
	 */
	private static final Map<Material,Float> molarMasses = molarMasses();
	/**
	 * the mass of one cm^3 of the material, in grams
	 */
	private static final Map<Material,Float> densities = densities();
	
	/**
	 * the energy required the raise the temperature of a block one degree kelvin
	 * @param mat
	 * @return
	 */
	public static float getHeatCapacityOfBlock(Material mat) {
		float blockMoles = getMolesInBlock(mat);
		float heatCap = blockMoles * specificHeats.get(mat);
		return heatCap;
	}
	
	public static float getMolesInBlock(Material mat) {
		final int ccInBlock = 1000000; //one million cubic centimeters in a block
		try {
			float blockMass = densities.get(mat) * ccInBlock;
			float blockMoles = blockMass/molarMasses.get(mat);
			return blockMoles;
		} catch(NullPointerException e) {
			return 0;
		}
	}
	
	private static Map<Material, Float> densities() {
		Map<Material,Float> result = new HashMap<>();
		result.put(Material.iron, 7.87f);
		result.put(Material.water, 1f);
		return result;
	}

	private static Map<Material, Float> molarMasses() {
		Map<Material,Float> result = new HashMap<>();
		result.put(Material.iron, 55.8f);
		result.put(Material.water, 18.0f);
		return result;
	}

	private static Map<Material,Float> specificHeats() {
		Map<Material,Float> result = new HashMap<>();
		result.put(Material.iron, 25.1f);
		result.put(Material.water, 4.18f);
		return result;
	}
	
}
