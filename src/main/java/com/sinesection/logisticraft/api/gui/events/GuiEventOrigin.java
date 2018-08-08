package com.sinesection.logisticraft.api.gui.events;

import java.util.Collection;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.api.gui.IElementGroup;
import com.sinesection.logisticraft.api.gui.IGuiElement;

public enum GuiEventOrigin {
	ANY {
		@Override
		public boolean isOrigin(IGuiElement origin, @Nullable IGuiElement element) {
			return true;
		}
	},
	SELF {
		@Override
		public boolean isOrigin(IGuiElement origin, @Nullable IGuiElement element) {
			return element == origin;
		}
	},
	PARENT {
		@Override
		public boolean isOrigin(IGuiElement origin, @Nullable IGuiElement element) {
			return element != null && element.getParent() == origin;
		}
	},
	DIRECT_CHILD {
		@Override
		public boolean isOrigin(IGuiElement origin, @Nullable IGuiElement element) {
			if (element == null || !(element instanceof IElementGroup)) {
				return false;
			}
			Collection<IGuiElement> elements = ((IElementGroup) element).getElements();
			return elements.contains(origin);
		}
	};

	public abstract boolean isOrigin(IGuiElement origin, @Nullable IGuiElement element);
}