package com.sinesection.logisticraft.power;

import net.minecraft.block.ITileEntityProvider;

public interface IHeatBlock extends ITileEntityProvider {

	/**
	 * @return The volume of the block, in {@code Cubic Meters(m^3)}. If not
	 *         overridden, it returns {@code 1f}.
	 */
	public default float getVolume() {
		return 1f;
	}

}
