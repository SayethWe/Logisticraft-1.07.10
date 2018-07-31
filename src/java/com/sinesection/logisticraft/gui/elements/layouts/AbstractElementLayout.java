package com.sinesection.logisticraft.gui.elements.layouts;

import com.sinesection.logisticraft.api.gui.IElementLayout;
import com.sinesection.logisticraft.api.gui.IElementLayoutHelper;
import com.sinesection.logisticraft.api.gui.IElementLayoutHelper.LayoutFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class AbstractElementLayout extends ElementGroup implements IElementLayout {
	/* The distance between the different elements of this group. */
	public int distance;

	public AbstractElementLayout(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}

	public AbstractElementLayout setDistance(int distance) {
		this.distance = distance;
		return this;
	}

	@Override
	public int getDistance() {
		return distance;
	}

	@Override
	public int getSize() {
		return elements.size();
	}
}