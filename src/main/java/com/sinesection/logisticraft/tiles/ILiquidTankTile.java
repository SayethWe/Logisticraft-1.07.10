package com.sinesection.logisticraft.tiles;

import com.sinesection.logisticraft.api.core.ILocatable;
import com.sinesection.logisticraft.fluid.ITankManager;

public interface ILiquidTankTile extends ILocatable {
	ITankManager getTankManager();
}