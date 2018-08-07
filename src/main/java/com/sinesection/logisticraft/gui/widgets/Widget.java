package com.sinesection.logisticraft.gui.widgets;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.gui.tooltips.IToolTipProvider;
import com.sinesection.logisticraft.gui.tooltips.ToolTip;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Widget implements IToolTipProvider {
	protected final WidgetManager manager;
	protected final int xPos;
	protected final int yPos;
	protected int width = 16;
	protected int height = 16;

	public Widget(WidgetManager manager, int xPos, int yPos) {
		this.manager = manager;
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}

	public abstract void draw(int startX, int startY);

	public void update(int mouseX, int mouseY) {

	}

	@Nullable
	@Override
	public ToolTip getToolTip(int mouseX, int mouseY) {
		return null;
	}

	@Override
	public boolean isToolTipVisible() {
		return false;
	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
		return false;
	}

	public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
	}

	public boolean handleMouseRelease(int mouseX, int mouseY, int eventType) {
		return false;
	}

	public void handleMouseMove(int mouseX, int mouseY, int mouseButton, long time) {
	}

}
