package com.sinesection.logisticraft.power;

public interface HeatSupplier {

	public int getCurrentTemp();
	
	public boolean canOutputTo(int dir);
	
}
