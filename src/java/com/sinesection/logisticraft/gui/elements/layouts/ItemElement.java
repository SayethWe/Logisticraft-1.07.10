package com.sinesection.logisticraft.gui.elements.layouts;

import com.sinesection.logisticraft.api.gui.IItemElement;
import com.sinesection.logisticraft.gui.elements.AbstractItemElement;

import net.minecraft.item.ItemStack;

public class ItemElement extends AbstractItemElement implements IItemElement {
	/* Attributes */
	private ItemStack stack;

	public ItemElement(int xPos, int yPos, ItemStack stack) {
		super(xPos, yPos);
		this.stack = stack;
	}

	@Override
	public ItemStack getStack() {
		return stack;
	}

	@Override
	public IItemElement setStack(ItemStack stack) {
		this.stack = stack;
		return this;
	}
}
