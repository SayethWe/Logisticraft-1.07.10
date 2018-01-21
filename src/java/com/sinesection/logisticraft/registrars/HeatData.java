package com.sinesection.logisticraft.registrars;

public class HeatData {
	
	public enum HeatDataType {
		BLOCK, MATERIAL
	}
	
	public static final float NULL_VALUE = Float.MIN_VALUE;
	
	public static final int SPECIFIC_HEAT_INDEX = 0;
	public static final int MASS_INDEX = 1;
	public static final int BASE_TEMP_INDEX = 2;
	public static final int DENSITY_INDEX = 3;
	
	private final HeatDataType type;
	private float[] data = new float[4]; // specificHeat = NULL_VALUE, mass = NULL_VALUE, baseTemp = NULL_VALUE, density = NULL_VALUE;
	
	private HeatData(HeatDataType type) {
		this.type = type;
		for(int i = 0; i < data.length; i++) {
			data[i] = NULL_VALUE;
		}
	}
	
	public static HeatData GenerateBlockHeatData(float specificHeat, float mass, float baseTemp) {
		HeatData hd = new HeatData(HeatDataType.BLOCK);
		hd.data[SPECIFIC_HEAT_INDEX] = specificHeat;
		hd.data[MASS_INDEX] = mass;
		hd.data[BASE_TEMP_INDEX] = baseTemp;
		return hd;
	}
	
	public static HeatData GenerateBlockHeatData(float specificHeat, float mass) {
		HeatData hd = new HeatData(HeatDataType.BLOCK);
		hd.data[SPECIFIC_HEAT_INDEX] = specificHeat;
		hd.data[MASS_INDEX] = mass;
		return hd;
	}
	
	public static HeatData GenerateBlockHeatData(float baseTemp) {
		HeatData hd = new HeatData(HeatDataType.BLOCK);
		hd.data[BASE_TEMP_INDEX] = baseTemp;
		return hd;
	}

	public static HeatData GenerateMaterialHeatData(float specificHeat, float density, float baseTemp) {
		HeatData hd = new HeatData(HeatDataType.MATERIAL);
		hd.data[SPECIFIC_HEAT_INDEX] = specificHeat;
		hd.data[DENSITY_INDEX] = density;
		hd.data[BASE_TEMP_INDEX] = baseTemp;
		return hd;
	}
	
	public static HeatData GenerateMaterialHeatData(float specificHeat, float density) {
		HeatData hd = new HeatData(HeatDataType.MATERIAL);
		hd.data[SPECIFIC_HEAT_INDEX] = specificHeat;
		hd.data[DENSITY_INDEX] = density;
		return hd;
	}
	
	public static HeatData GenerateMaterialHeatData(float baseTemp) {
		HeatData hd = new HeatData(HeatDataType.MATERIAL);
		hd.data[BASE_TEMP_INDEX] = baseTemp;
		return hd;
	}

	public boolean canStoreHeat() {
		return this.type == HeatDataType.BLOCK && this.data[SPECIFIC_HEAT_INDEX] != NULL_VALUE;
	}
	
	public boolean hasSpecificHeat() {
		return this.data[SPECIFIC_HEAT_INDEX] != NULL_VALUE;
	}

	public float getSpecificHeat() {
		return this.data[SPECIFIC_HEAT_INDEX];
	}
	
	public boolean hasMass() {
		return this.data[MASS_INDEX] != NULL_VALUE;
	}

	public float getMass() {
		return this.data[MASS_INDEX];
	}
	
	public boolean hasBaseTemp() {
		return this.data[BASE_TEMP_INDEX] != NULL_VALUE;
	}

	public float getBaseTemp() {
		return this.data[BASE_TEMP_INDEX];
	}
	
	public boolean hasDensity() {
		return this.data[DENSITY_INDEX] != NULL_VALUE;
	}
	
	public float getDensity() {
		return this.data[DENSITY_INDEX];
	}

	public HeatDataType getType() {
		return type;
	}

}
