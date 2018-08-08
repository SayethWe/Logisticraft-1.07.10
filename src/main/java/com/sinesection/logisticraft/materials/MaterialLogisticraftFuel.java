package com.sinesection.logisticraft.materials;

import net.minecraft.block.material.Material;

public class MaterialLogisticraftFuel extends LogisticraftMaterial {

	public MaterialLogisticraftFuel() {
		super(Material.water.getMaterialMapColor());
	}

	@Override
	public boolean isLiquid() {
		return true;
	}

	@Override
	public boolean blocksMovement() {
		return false;
	}

	@Override
	public boolean getCanBurn() {
		return true;
	}

	@Override
	public boolean isReplaceable() {
		return true;
	}

	@Override
	public int getMaterialMobility() {
		return 2;
	}

}
