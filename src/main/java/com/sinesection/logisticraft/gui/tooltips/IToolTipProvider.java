package com.sinesection.logisticraft.gui.tooltips;

import javax.annotation.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IToolTipProvider {
	@Nullable
	@SideOnly(Side.CLIENT)
	ToolTip getToolTip(int mouseX, int mouseY);

	@SideOnly(Side.CLIENT)
	boolean isToolTipVisible();

	@SideOnly(Side.CLIENT)
	boolean isMouseOver(int mouseX, int mouseY);

	@SideOnly(Side.CLIENT)
	default boolean isRelativeToGui() {
		return true;
	}
}
