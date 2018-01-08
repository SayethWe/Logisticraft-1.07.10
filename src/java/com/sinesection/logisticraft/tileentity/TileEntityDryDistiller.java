package com.sinesection.logisticraft.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileEntityDryDistiller extends TileEntity {
	
	private String localizedName;
	
	public void setGuiDisplayName(String displayName) {
		this.localizedName = displayName;
	}

}
