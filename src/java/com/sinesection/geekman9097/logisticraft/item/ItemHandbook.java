package com.sinesection.geekman9097.logisticraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHandbook extends LogisticraftItem {
	
	public ItemHandbook() {
		super("handbook");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World w, EntityPlayer p) {
		return null;
	}

}
