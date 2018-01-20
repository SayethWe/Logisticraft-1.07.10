package com.sinesection.logisticraft.power;

import com.sinesection.logisticraft.block.LogisticraftBlock;

import net.minecraft.block.material.Material;

public abstract class LogisticraftHeatBlock extends LogisticraftBlock {

	protected int temp;
	protected int energy;
	
	protected LogisticraftHeatBlock(String name, String textureName, String registryName, Material mat) {
		super(name, textureName, registryName, mat);
	}

	public LogisticraftHeatBlock(String universalName) {
		this(universalName, universalName, universalName, Material.iron);
	}
	
	public int getCurrentTemp() {
		return temp;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public boolean takeEnergy(int tempRequested) {
		if(tempRequested <= temp) {
			energy -= tempRequested;
			ensureTempStatus();
			return true;
		} else {
			return false;
		}
	}
	
	public void addEnergy(int tempIn) {
		energy += tempIn;
		temp = Math.max(temp, tempIn);
	}
	
	private void ensureTempStatus() {
		if(temp > energy) temp = energy;
	}

}
