package com.sinesection.logisticraft.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.gui.GuiLogisticraft;

import net.minecraft.client.Minecraft;

public class WidgetManager {

	public final GuiLogisticraft gui;
	public final Minecraft minecraft;
	protected final List<Widget> widgets = new ArrayList<>();

	public WidgetManager(GuiLogisticraft gui) {
		this.gui = gui;
		this.minecraft = Minecraft.getMinecraft();
	}

	public void add(Widget slot) {
		this.widgets.add(slot);
	}

	public void remove(Widget slot) {
		this.widgets.remove(slot);
	}

	public void clear() {
		this.widgets.clear();
	}

	public List<Widget> getWidgets() {
		return widgets;
	}

	@Nullable
	public Widget getAtPosition(int mX, int mY) {
		for (Widget slot : widgets) {
			if (slot.isMouseOver(mX, mY)) {
				return slot;
			}
		}

		return null;
	}

	public void drawWidgets() {
		for (Widget slot : widgets) {
			slot.draw(0, 0);
		}
	}

	public void updateWidgets(int mouseX, int mouseY) {
		for (Widget slot : widgets) {
			slot.update(mouseX, mouseY);
		}
	}

	public void handleMouseClicked(int mouseX, int mouseY, int mouseButton) {
		Widget slot = getAtPosition(mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop());
		if (slot != null) {
			slot.handleMouseClick(mouseX, mouseY, mouseButton);
		}
	}

	public boolean handleMouseRelease(int mouseX, int mouseY, int eventType) {
		boolean hasToStop = false;
		for (Widget slot : widgets) {
			hasToStop |= slot.handleMouseRelease(mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop(), eventType);
		}
		return hasToStop;
	}

	public void handleMouseMove(int mouseX, int mouseY, int mouseButton, long time) {
		for (Widget slot : widgets) {
			slot.handleMouseMove(mouseX, mouseY, mouseButton, time);
		}
	}

}
