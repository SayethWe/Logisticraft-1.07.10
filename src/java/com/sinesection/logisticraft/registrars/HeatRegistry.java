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

	private static final Map<Block, HeatData> heatDataBlockMap = new HashMap<Block, HeatData>();
	private static final Map<Material, HeatData> heatDataMaterialMap = new HashMap<Material, HeatData>();

	static {
		registerHeatData(Blocks.iron_block, 25.1f, 7.87e3f, 0f);
		registerHeatData(Blocks.water, 4.18f, 1e3f, 0f);
		registerHeatData(Blocks.lava, 973f);
		registerHeatData(Material.iron, 25.1f, 7.87e3f, 0f);
		registerHeatData(Material.water, 4.18f, 1e3f, 0f);
		registerHeatData(Material.lava, 973f);
	}

	/**
	 * Registers a block to LogistiCraft's heat registry.
	 * 
	 * @param specificHeat
	 *            The amount of energy in Joules(J) it takes to raise the
	 *            temperature of one mole of the block by one degree Kelvin(K).
	 * @param mass
	 *            The amount the specified block has, in Kilograms(kg).
	 * @param baseTemp
	 *            The base temperature of a specific block, in Kelvin(K).
	 */
	public static void registerHeatData(Block block, float specificHeat, float mass, float baseTemp) {
		if (!heatDataBlockMap.containsKey(block))
			heatDataBlockMap.put(block, HeatData.GenerateBlockHeatData(specificHeat, mass, baseTemp));
	}

	/**
	 * Registers a block to LogistiCraft's heat registry.
	 * 
	 * @param specificHeat
	 *            The amount of energy in Joules(J) it takes to raise the
	 *            temperature of one mole of the block by one degree Kelvin(K).
	 * @param mass
	 *            The amount the specified block has, in Kilograms(kg).
	 */
	public static void registerHeatData(Block block, float specificHeat, float mass) {
		if (!heatDataBlockMap.containsKey(block))
			heatDataBlockMap.put(block, HeatData.GenerateBlockHeatData(specificHeat, mass));
	}

	/**
	 * Registers a block to LogistiCraft's heat registry.
	 * 
	 * @param specificHeat
	 *            The amount of energy in Joules(J) it takes to raise one mole
	 *            by one degree Kelvin(K).
	 * @param mass
	 *            The amount the specified block has, in Kilograms(kg).
	 * @param baseTemp
	 *            The base temperature of a specific block, in Kelvin(K).
	 */
	public static void registerHeatData(Block block, float baseTemp) {
		if (!heatDataBlockMap.containsKey(block))
			heatDataBlockMap.put(block, HeatData.GenerateBlockHeatData(baseTemp));
	}

	/**
	 * Registers a material to LogistiCraft's heat registry.
	 * 
	 * @param specificHeat
	 *            The amount of energy in Joules(J) it takes to raise the
	 *            temperature of the material one mole by one degree Kelvin(K).
	 * @param density
	 *            The density of the material, in Kilograms per Cubic
	 *            Meter(Kg/m^3).
	 * @param baseTemp
	 *            The base temperature of a specific material, in Kelvin(K).
	 */
	public static void registerHeatData(Material mat, float specificHeat, float density, float baseTemp) {
		if (!heatDataMaterialMap.containsKey(mat))
			heatDataMaterialMap.put(mat, HeatData.GenerateMaterialHeatData(specificHeat, density, baseTemp));
	}

	/**
	 * Registers a material to LogistiCraft's heat registry.
	 * 
	 * @param specificHeat
	 *            The amount of energy in Joules(J) it takes to raise the
	 *            temperature of the material one mole by one degree Kelvin(K).
	 * @param density
	 *            The density of the material, in Kilograms per Cubic
	 *            Meter(Kg/m^3).
	 */
	public static void registerHeatData(Material mat, float specificHeat, float density) {
		if (!heatDataMaterialMap.containsKey(mat))
			heatDataMaterialMap.put(mat, HeatData.GenerateMaterialHeatData(specificHeat, density));
	}

	/**
	 * Registers a material to LogistiCraft's heat registry.
	 * 
	 * @param baseTemp
	 *            The base temperature of a specific material, in Kelvin(K).
	 */
	public static void registerHeatData(Material mat, float baseTemp) {
		if (!heatDataMaterialMap.containsKey(mat))
			heatDataMaterialMap.put(mat, HeatData.GenerateMaterialHeatData(baseTemp));
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
		HeatData hd = getHeatData(bl);
		if (hd == null)
			return 0f;
		if (hd.canStoreHeat())
			return hd.getMass() * hd.getSpecificHeat();
		else if (bl instanceof IHeatBlock)
			return hd.getDensity() * ((IHeatBlock) bl).getVolume() * hd.getSpecificHeat();
		else
			return hd.getDensity() * hd.getSpecificHeat();
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
		HeatData hd;
		if (te != null)
			if (te instanceof TileEntityHeatable)
				return ((TileEntityHeatable) te).getCurrentTemp();
			else if ((hd = getHeatData(bl)) != null) {
				if (hd.getType() == null)
					return NULL_VALUE;
				if (hd.hasBaseTemp())
					return hd.getBaseTemp();
				return AMBIENT_TEMPERATURE;
			}
		return NULL_VALUE;
	}

	public static HeatData getHeatData(Block bl) {
		if (bl == null)
			return null;
		HeatData hd;
		if ((hd = heatDataBlockMap.get(bl)) != null)
			return hd;
		else if ((hd = heatDataMaterialMap.get(bl.getMaterial())) != null)
			return hd;
		return null;
	}
}
