package com.sinesection.logisticraft.power;

import net.minecraft.block.ITileEntityProvider;

public interface IHeatBlock extends ITileEntityProvider {
	
//	/**
//	 * 
//	 * @return The volume of the block, in {@code Kelvin(K)}. If not overridden, it returns {@link com.sinesection.logisticraft.registrars.HeatRegistry#AMBIENT_TEMPERATURE}. 
//	 */
//	public default float getCurrentTemp() {
//		return HeatRegistry.AMBIENT_TEMPERATURE;
//	}
//	
//	public default float getCurrentEnergy() {
//		return HeatRegistry.AMBIENT_TEMPERATURE;
//	}
//	
//	public default boolean takeEnergy(int tempRequested) {
//		return takeEnergy(tempRequested, true);
//	}
//	
//	public default boolean takeEnergy(int tempRequested, boolean changeTemp) {
//		if(tempRequested <= temp) {
//			if(changeTemp) {
//				energy -= tempRequested;
//				ensureTempStatus();
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
//	public void addEnergy(int tempIn) {
//		energy += tempIn;
//		temp = Math.max(temp, tempIn);
//	}
//	
//	private void ensureTempStatus() {
//		if(temp > energy) temp = energy;
//	}
	
	/**
	 * @return The volume of the block, in {@code Cubic Meters(m^3)}. If not overridden, it returns {@code 1f}. 
	 */
	public default float getVolume() {
		return 1f;
	}
	

}
