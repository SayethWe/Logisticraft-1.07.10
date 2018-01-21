package com.sinesection.logisticraft.block.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileEntityHeatable extends TileEntity {
	
	private float temp;

	public float getCurrentTemp() {
		return temp;
	}

}
