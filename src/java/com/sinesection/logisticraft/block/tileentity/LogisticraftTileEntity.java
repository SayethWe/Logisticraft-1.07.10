package com.sinesection.logisticraft.block.tileentity;

import net.minecraft.tileentity.TileEntity;

public abstract class LogisticraftTileEntity extends TileEntity {
	
	public void markForUpdate() {
	    this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

}
