package com.sinesection.logisticraft.api.gui.events;

import com.sinesection.logisticraft.api.gui.IGuiElement;

public abstract class GuiElementEvent {
	private final IGuiElement origin;

	public GuiElementEvent(IGuiElement origin) {
		this.origin = origin;
	}

	public final IGuiElement getOrigin() {
		return origin;
	}

	public final boolean isOrigin(IGuiElement element) {
		return this.origin == element;
	}
}