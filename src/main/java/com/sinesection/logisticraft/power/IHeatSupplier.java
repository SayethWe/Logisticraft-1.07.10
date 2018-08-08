package com.sinesection.logisticraft.power;

import net.minecraftforge.common.util.ForgeDirection;

public interface IHeatSupplier extends IHeatBlock {

	public boolean canOutputTo(ForgeDirection dir);

}
