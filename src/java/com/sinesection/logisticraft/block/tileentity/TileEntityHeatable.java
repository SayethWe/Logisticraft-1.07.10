package com.sinesection.logisticraft.block.tileentity;

import com.sinesection.logisticraft.power.IHeatable;
import com.sinesection.logisticraft.registrars.HeatProperties;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityHeatable extends TileEntity implements IHeatable {
	
	private float temp, energy;
	private final HeatProperties heatProperties;
	
	public TileEntityHeatable(HeatProperties hp) {
		this.heatProperties = hp;
	}
	
	@Override
	public float getCurrentTemp() {
		return temp;
	}
	
	public void updateEntity() {
		super.updateEntity();
	}

	public float getRequiredTemp() {
		return 0;
	}

	@Override
	public float getCurrentEnergy() {
		return energy;
	}

	@Override
	public float getMaxEnergy() {
		return 0;
	}

	@Override
	public boolean takeEnergy(float tempRequested, boolean changeTemp, ForgeDirection side) {
		return false;
	}

	@Override
	public boolean addEnergy(float tempIn, boolean changeTemp, ForgeDirection side) {
		return false;
	}

	@Override
	public boolean isReceivingEnergy() {
		return false;
	}

	@Override
	public boolean isLosingEnergy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canInputFrom(ForgeDirection dir) {
		return false;
	}

	@Override
	public boolean canOutputTo(ForgeDirection dir) {
		return false;
	}
	
}
