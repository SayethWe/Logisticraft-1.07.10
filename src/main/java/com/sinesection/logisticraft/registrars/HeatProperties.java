package com.sinesection.logisticraft.registrars;

public class HeatProperties {

	public enum HeatDataType {
		BLOCK,
		MATERIAL
	}

	public static final float NULL_VALUE = Float.MIN_VALUE;

	public static final int SPECIFIC_HEAT_INDEX = 0;
	public static final int MASS_INDEX = 1;
	public static final int BASE_TEMP_INDEX = 2;
	public static final int DENSITY_INDEX = 3;
	public static final int CONDUCTANCE_INDEX = 4;

	private final HeatDataType type;
	private float[] data = new float[5];

	private HeatProperties(HeatDataType type) {
		this.type = type;
		for (int i = 0; i < data.length; i++) {
			data[i] = NULL_VALUE;
			if (i == CONDUCTANCE_INDEX)
				data[i] = 0f;
		}
	}

	public static HeatProperties GenerateBlockHeatProperties(float specificHeat, float mass, float conductance, float baseTemp) {
		HeatProperties hd = new HeatProperties(HeatDataType.BLOCK);
		hd.data[SPECIFIC_HEAT_INDEX] = specificHeat;
		hd.data[MASS_INDEX] = mass;
		hd.data[BASE_TEMP_INDEX] = baseTemp;
		hd.data[CONDUCTANCE_INDEX] = conductance;
		return hd;
	}

	public static HeatProperties GenerateBlockHeatProperties(float specificHeat, float mass, float conductance) {
		HeatProperties hd = new HeatProperties(HeatDataType.BLOCK);
		hd.data[SPECIFIC_HEAT_INDEX] = specificHeat;
		hd.data[MASS_INDEX] = mass;
		hd.data[CONDUCTANCE_INDEX] = conductance;
		return hd;
	}

	public static HeatProperties GenerateBlockHeatProperties(float baseTemp, float conductance) {
		HeatProperties hd = new HeatProperties(HeatDataType.BLOCK);
		hd.data[BASE_TEMP_INDEX] = baseTemp;
		hd.data[CONDUCTANCE_INDEX] = conductance;
		return hd;
	}

	public static HeatProperties GenerateBlockHeatProperties(float conductance) {
		HeatProperties hd = new HeatProperties(HeatDataType.BLOCK);
		hd.data[CONDUCTANCE_INDEX] = conductance;
		return hd;
	}

	public static HeatProperties GenerateMaterialHeatProperties(float specificHeat, float density, float conductance, float baseTemp) {
		HeatProperties hd = new HeatProperties(HeatDataType.MATERIAL);
		hd.data[SPECIFIC_HEAT_INDEX] = specificHeat;
		hd.data[DENSITY_INDEX] = density;
		hd.data[BASE_TEMP_INDEX] = baseTemp;
		hd.data[CONDUCTANCE_INDEX] = conductance;
		return hd;
	}

	public static HeatProperties GenerateMaterialHeatProperties(float specificHeat, float density, float conductance) {
		HeatProperties hd = new HeatProperties(HeatDataType.MATERIAL);
		hd.data[SPECIFIC_HEAT_INDEX] = specificHeat;
		hd.data[DENSITY_INDEX] = density;
		hd.data[CONDUCTANCE_INDEX] = conductance;
		return hd;
	}

	public static HeatProperties GenerateMaterialHeatProperties(float baseTemp, float conductance) {
		HeatProperties hd = new HeatProperties(HeatDataType.MATERIAL);
		hd.data[BASE_TEMP_INDEX] = baseTemp;
		hd.data[CONDUCTANCE_INDEX] = conductance;
		return hd;
	}

	public static HeatProperties GenerateMaterialHeatProperties(float conductance) {
		HeatProperties hd = new HeatProperties(HeatDataType.MATERIAL);
		hd.data[CONDUCTANCE_INDEX] = conductance;
		return hd;
	}

	public boolean canStoreHeat() {
		return this.type == HeatDataType.BLOCK && this.data[SPECIFIC_HEAT_INDEX] != NULL_VALUE;
	}

	public boolean canConductHeat() {
		return this.data[CONDUCTANCE_INDEX] > 0f;
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

	public float getConductance() {
		return this.data[CONDUCTANCE_INDEX];
	}

	public HeatDataType getType() {
		return type;
	}

}
