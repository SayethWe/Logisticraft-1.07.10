package com.sinesection.geekman9097.logisticraft.crafting;

import com.sinesection.geekman9097.logisticraft.item.ModItems;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class LogisticraftVanillaCrafting {

	public static void registerCrafting() {
		GameRegistry.addRecipe(new ItemStack(ModItems.roadWheel,2), new Object[] {" R ", "RIR", " R ", 'R', ModItems.refinedRubber, 'I', Items.iron_ingot});
	}
}
