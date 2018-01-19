package com.sinesection.utils;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
/**
 * all values to three sigfigs
 * @author geekman9097
 *
 */
public class MaterialHeatProperties {

	/**
	 * the amount of energy in joules it takes to raise one mole by one degree kelvin
	 */
	private static final Map<Block,Float> specificHeats = specificHeats();
	/**
	 * the amount the specified block weighs, in kilograms
	 */
	private static final Map<Block,Float> masses = masses();
	/**
	 * the mass of one cm^3 of the Block, in grams
	 */
	
	/**
	 * the energy required the raise the temperature of a block one degree kelvin
	 * @param bl
	 * @return
	 */
	public static float getHeatCapacityOfBlock(Block bl) {
		try {
			float blockMass = masses.get(bl);
			float heatCap = blockMass * specificHeats.get(bl);
			return heatCap;
		} catch (NullPointerException e) {
			//we either don't have data, or we're checking a block that doesn't exist.
			return 0;
		}
	}

	private static Map<Block, Float> masses() {
		Map<Block,Float> result = new HashMap<>();
		result.put(Blocks.iron_block, 7.87e3f);
		result.put(Blocks.water, 1e3f);
		return result;
	}

	private static Map<Block,Float> specificHeats() {
		Map<Block,Float> result = new HashMap<>();
		result.put(Blocks.iron_block, 25.1f);
		result.put(Blocks.water, 4.18f);
		return result;
	}
	
}
