package com.sinesection.logisticraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHandbook extends LogisticraftItem {
	
	public ItemHandbook() {
		super("handbook");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World w, EntityPlayer p) {
		p.setHealth(5);
		p.setAbsorptionAmount(20);
//		p.openGui(mod, modGuiId, world, x, y, z);
		return itemstack;
		
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
	
	

}
