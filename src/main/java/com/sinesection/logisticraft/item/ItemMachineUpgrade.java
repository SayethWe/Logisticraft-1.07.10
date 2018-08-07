package com.sinesection.logisticraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMachineUpgrade extends LogisticraftItem {

	public ItemMachineUpgrade() {
		super("tankUpgrade");
	}

	@Override
	public boolean onItemUseFirst(ItemStack is, EntityPlayer ep, World world, int x, int y, int z, int s, float par8, float par9, float par10) {
		return false;
	}

}
