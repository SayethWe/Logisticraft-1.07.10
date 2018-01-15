package com.sinesection.logisticraft.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class BucketSlot extends Slot {

	public BucketSlot(IInventory inventory, int slot, int x, int y) {
		super(inventory, slot, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return FluidContainerRegistry.isContainer(stack);
	}

}
