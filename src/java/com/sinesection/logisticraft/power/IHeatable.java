package com.sinesection.logisticraft.power;

import net.minecraftforge.common.util.ForgeDirection;

public interface IHeatable {

	public float getCurrentTemp();
	public float getRequiredTemp();
	public float getCurrentEnergy();
	public float getMaxEnergy();
	public boolean isReceivingEnergy();
	public boolean isLosingEnergy();
	public boolean takeEnergy(float tempRequested, boolean changeTemp, ForgeDirection side);
	public boolean addEnergy(float tempIn, boolean changeTemp, ForgeDirection side);
	public boolean canInputFrom(ForgeDirection side);
	public boolean canOutputTo(ForgeDirection side);

}
