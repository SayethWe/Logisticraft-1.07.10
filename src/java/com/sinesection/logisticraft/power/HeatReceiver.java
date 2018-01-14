package com.sinesection.logisticraft.power;

public interface HeatReceiver {

	public int getRequiredTemp();
	
	public boolean isReceiving();
	
	public boolean canInputFrom(int dir);
}
