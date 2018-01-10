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
//		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.dryDistillerIdle), new Object[] {"SIS","I I","SNS", 'S', Blocks.stone, 'I', Items.iron_ingot, 'N', Blocks.netherrack);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.dryDistillerIdle), new Object[] {"SIS","IFI","SNS", 'S', Blocks.stone, 'I', Items.iron_ingot, 'N', Blocks.netherrack, 'F', Blocks.furnace});
//		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.dryDistillerIdle), new Object[] {"SHS","IFI","SNS", 'S', Blocks.stone, 'I', Items.iron_ingot, 'N', Blocks.netherrack, 'F', Blocks.furnace, 'H', Blocks.hopper});
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.roadBlock,6), new Object[] {"SPS", "GPG", "CPC", 'S', Blocks.sand, 'P', ModItems.tar, 'G', Blocks.gravel, 'C', Blocks.cobblestone});
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.trafficDirector), new Object[] {"III", "ISI", " T ", 'I', Items.iron_ingot, 'S', Items.sign, 'T', Items.stick});
	}
	public static void registerShapelessCrafting() {
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.handbook), Items.book, ModItems.shippingOrder);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.shippingOrder,4), Items.paper, new ItemStack(Items.dye,1,0),ModItems.sulfur);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.rubberBlock), ModItems.refinedRubber, ModItems.refinedRubber,
													 ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber,
													 ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.refinedRubber, 8), new ItemStack(ModBlocks.rubberBlock));
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.roadBlock), ModItems.tar, Blocks.cobblestone, Blocks.gravel, Blocks.sand);
	}
	
	public static void registerFurnaceCrafting() {
		GameRegistry.addSmelting(new ItemStack(Items.egg), new ItemStack(ModItems.sulfur), 0.8f);
	}
}
