package com.sinesection.logisticraft.gui.events;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.api.gui.IGuiElement;
import com.sinesection.logisticraft.api.gui.events.GuiElementEvent;

public class EventValueChanged<V> extends GuiElementEvent {
	@Nullable
	private final V value;

	public EventValueChanged(IGuiElement origin, V value) {
		super(origin);
		this.value = value;
	}

	@Nullable
	public V getValue() {
		return value;
	}
}