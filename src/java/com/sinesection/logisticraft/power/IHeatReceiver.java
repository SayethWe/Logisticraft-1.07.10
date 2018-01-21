package com.sinesection.logisticraft.power;

import net.minecraftforge.common.util.ForgeDirection;

public interface IHeatReceiver extends IHeatBlock {

	public int getRequiredTemp();
	
	public boolean isReceiving();
	
	public boolean canInputFrom(ForgeDirection dir);
}
