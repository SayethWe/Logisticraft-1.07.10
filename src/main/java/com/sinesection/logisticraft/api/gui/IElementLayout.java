package com.sinesection.logisticraft.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IElementLayout extends IElementGroup {
	/**
	 * @param distance
	 * @return
	 */
	IElementLayout setDistance(int distance);

	/**
	 * @return The distance between the different elements of this layout.
	 */
	int getDistance();

	int getSize();
}
