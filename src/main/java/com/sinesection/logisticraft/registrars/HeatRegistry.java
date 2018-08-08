package com.sinesection.logisticraft.registrars;

import java.util.HashMap;
import java.util.Map;

import com.sinesection.logisticraft.block.tileentity.TileEntityHeatable;
import com.sinesection.logisticraft.power.IHeatBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * all values to three sigfigs
 * 
 * @author geekman9097
 *
 */
public class HeatRegistry {

	public static final float NULL_VALUE = Float.MIN_VALUE;
	public static final float AMBIENT_TEMPERATURE = 294f;

	private static final Map<Block, HeatProperties> heatDataBlockMap = new HashMap<Block, HeatProperties>();
	private static final Map<Material, HeatProperties> heatDataMaterialMap = new HashMap<Material, HeatProperties>();

	static {
		registerHeatData(Blocks.iron_block, 25.1f, 7.87e3f, 79.5f);
		registerHeatData(Blocks.water, 4.18f, 1e3f, 0.6f);
		registerHeatData(Blocks.lava, 0.8f, 973f);
		registerHeatData(Material.iron, 25.1f, 7.87e3f, 79.5f);
		registerHeatData(Material.water, 0.6f);
		registerHeatData(Material.lava, 973f, 0.8f);
	}

	public static void registerHeatData(Block block, float specificHeat, float mass, float conductance, float baseTemp) {
		if (!heatDataBlockMap.containsKey(block))
			heatDataBlockMap.put(block, HeatProperties.GenerateBlockHeatProperties(specificHeat, mass, conductance, baseTemp));
	}

	public static void registerHeatData(Block block, float specificHeat, float mass, float conductance) {
		if (!heatDataBlockMap.containsKey(block))
			heatDataBlockMap.put(block, HeatProperties.GenerateBlockHeatProperties(specificHeat, mass, conductance));
	}

	public static void registerHeatData(Block block, float baseTemp, float conductance) {
		if (!heatDataBlockMap.containsKey(block))
			heatDataBlockMap.put(block, HeatProperties.GenerateBlockHeatProperties(baseTemp, conductance));
	}

	public static void registerHeatData(Block block, float conductance) {
		if (!heatDataBlockMap.containsKey(block))
			heatDataBlockMap.put(block, HeatProperties.GenerateBlockHeatProperties(conductance));
	}

	public static void registerHeatData(Material mat, float specificHeat, float density, float conductance, float baseTemp) {
		if (!heatDataMaterialMap.containsKey(mat))
			heatDataMaterialMap.put(mat, HeatProperties.GenerateMaterialHeatProperties(specificHeat, density, conductance, baseTemp));
	}

	public static void registerHeatData(Material mat, float specificHeat, float conductance, float density) {
		if (!heatDataMaterialMap.containsKey(mat))
			heatDataMaterialMap.put(mat, HeatProperties.GenerateMaterialHeatProperties(specificHeat, density, conductance));
	}

	public static void registerHeatData(Material mat, float baseTemp, float conductance) {
		if (!heatDataMaterialMap.containsKey(mat))
			heatDataMaterialMap.put(mat, HeatProperties.GenerateMaterialHeatProperties(baseTemp, conductance));
	}

	public static void registerHeatData(Material mat, float conductance) {
		if (!heatDataMaterialMap.containsKey(mat))
			heatDataMaterialMap.put(mat, HeatProperties.GenerateMaterialHeatProperties(conductance));
	}

	/**
	 * the energy required the raise the temperature of a block one degree
	 * Kelvin(K).
	 * 
	 * @param bl
	 * @return
	 */
	public static float getHeatCapacityOfBlock(Block bl) {
		if (bl == null)
			return 0f;
		HeatProperties hd = getHeatData(bl);
		if (hd == null)
			return 0f;
		if (hd.canStoreHeat())
			if (bl instanceof IHeatBlock)
				return hd.getDensity() * ((IHeatBlock) bl).getVolume() * hd.getSpecificHeat();
			else
				return hd.getDensity() * hd.getSpecificHeat();
		else
			return 0f;
	}

	/**
	 * get the temperature of a block
	 * 
	 * @param bl
	 *            the block to check
	 * @return the current temperature, as a float.
	 */
	public static float getCurrentTemp(World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		Block bl = world.getBlock(x, y, z);
		HeatProperties hp;
		if (te != null)
			if (te instanceof TileEntityHeatable)
				return ((TileEntityHeatable) te).getCurrentTemp();
			else if ((hp = getHeatData(bl)) != null) {
				if (hp.getType() == null)
					return NULL_VALUE;
				if (hp.hasBaseTemp())
					return hp.getBaseTemp();
				return AMBIENT_TEMPERATURE;
			}
		return NULL_VALUE;
	}

	public static HeatProperties getHeatData(Block bl) {
		if (bl == null)
			return null;
		HeatProperties hp;
		if ((hp = heatDataBlockMap.get(bl)) != null)
			return hp;
		else if ((hp = heatDataMaterialMap.get(bl.getMaterial())) != null)
			return hp;
		return null;
	}
}
