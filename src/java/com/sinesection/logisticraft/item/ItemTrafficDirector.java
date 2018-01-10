package com.sinesection.logisticraft.item;

import net.minecraft.item.ItemStack;

public class ItemTrafficDirector extends LogisticraftItem{

	/**
	 * An item to change road types
	 * When used on a road, allows selection of desired variant
	 * Shift Click to rotate a road
	 * 
	 * POSSIBLE: Takes damage when done (click on different block)
	 */
	public ItemTrafficDirector() {
		super("trafficSign");
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
	
}
