package com.sinesection.logisticraft.block.TileEntity;

import net.minecraft.tileentity.TileEntity;

public class LogisticraftTileEntity extends TileEntity {

	private String registryName;
	
	public LogisticraftTileEntity(String registryName) {
		
	}
	
	public String getRegistryName() {
		return registryName;
	}
}
