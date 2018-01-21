package com.sinesection.logisticraft.block.tileentity;

import com.sinesection.logisticraft.power.IHeatable;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityHeatable extends TileEntity implements IHeatable {
	
	private float temp;

	public float getCurrentTemp() {
		return temp;
	}
	
	public void updateEntity() {
		super.updateEntity();
	}

	@Override
	public float getRequiredTemp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCurrentEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean takeEnergy(float tempRequested, boolean changeTemp, ForgeDirection side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addEnergy(float tempIn, ForgeDirection side) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReceivingEnergy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canInputFrom(ForgeDirection dir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canOutputTo(ForgeDirection dir) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
