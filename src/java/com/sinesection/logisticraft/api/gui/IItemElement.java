package com.sinesection.logisticraft.api.gui;

import net.minecraft.item.ItemStack;

public interface IItemElement extends IGuiElement {
	/**
	 * @return The contained {@link ItemStack}.
	 */
	ItemStack getStack();

	IItemElement setStack(ItemStack stack);
}