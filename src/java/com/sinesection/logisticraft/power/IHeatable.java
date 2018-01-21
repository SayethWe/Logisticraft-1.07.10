package com.sinesection.logisticraft.power;

import net.minecraftforge.common.util.ForgeDirection;

public interface IHeatable {

	public float getCurrentTemp();
	public float getRequiredTemp();
	public float getCurrentEnergy();
	public boolean takeEnergy(float tempRequested, boolean changeTemp, ForgeDirection side);
	public void addEnergy(float tempIn, ForgeDirection side);
	public boolean isReceivingEnergy();
	public boolean canInputFrom(ForgeDirection dir);
	public boolean canOutputTo(ForgeDirection dir);

}
