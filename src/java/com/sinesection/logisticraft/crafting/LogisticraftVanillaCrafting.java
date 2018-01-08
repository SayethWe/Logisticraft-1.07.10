package com.sinesection.logisticraft.crafting;

import com.sinesection.logisticraft.registrars.ModBlocks;
import com.sinesection.logisticraft.registrars.ModItems;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class LogisticraftVanillaCrafting {

	public static void registerCrafting() {
		GameRegistry.addRecipe(new ItemStack(ModItems.roadWheel,2), new Object[] {" R ", "RWR", " R ", 'R', ModItems.refinedRubber, 'W', Blocks.planks});
	}
	public static void registerShapelessCrafting() {
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.handbook), Items.book, ModItems.shippingOrder);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.shippingOrder,4), Items.paper, new ItemStack(Items.dye,1,0),ModItems.sulfur);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.rubberBlock), ModItems.refinedRubber, ModItems.refinedRubber,
													 ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber,
													 ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.refinedRubber,8), new ItemStack(ModBlocks.rubberBlock));
		
	}
	
	public static void registerFurnaceCrafting() {
		GameRegistry.addSmelting(new ItemStack(Items.egg), new ItemStack(ModItems.sulfur), 0.8f);
	}
}
