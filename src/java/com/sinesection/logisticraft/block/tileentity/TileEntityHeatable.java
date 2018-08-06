package com.sinesection.logisticraft.block.tileentity;

import com.sinesection.logisticraft.power.IHeatable;
import com.sinesection.logisticraft.registrars.HeatProperties;

import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityHeatable extends TileEntity implements IHeatable {
	
	protected float temp, energy;
	protected final HeatProperties heatProperties;
	
	public TileEntityHeatable(HeatProperties hp) {
		this.heatProperties = hp;
	}
	
	@Override
	public float getCurrentTemp() {
		return temp;
	}

	@Override
	public float getCurrentEnergy() {
		return energy;
	}
	
}
