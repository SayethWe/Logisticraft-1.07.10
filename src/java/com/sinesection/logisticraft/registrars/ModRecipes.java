package com.sinesection.logisticraft.registrars;

import com.google.common.collect.ImmutableMap;
import com.sinesection.logisticraft.api.crafting.RecipeManagers;
import com.sinesection.logisticraft.crafting.DryDistillerRecipeManager;
import com.sinesection.logisticraft.crafting.FractionatorRecipeManager;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

	private static void setupManagers() {
		RecipeManagers.dryDistillerManager = new DryDistillerRecipeManager();
		RecipeManagers.fractionatorManager = new FractionatorRecipeManager();
	}
	
	public static void loadRecipes() {
		setupManagers();
		
		registerCrafting();
		registerOreDictCrafting();
		registerShapelessCrafting();
		registerFurnaceCrafting();
		
		registerMachineCrafting();
	}
	
	private static void registerMachineCrafting() {
		RecipeManagers.fractionatorManager.addRecipeWithOptionalFluidBonus(100, new ItemStack(Items.egg), new FluidStack(FluidRegistry.WATER, 50), ImmutableMap.of(
				new ItemStack(ModItems.sulfur, 3), 1f, 		// 100% chance to get 3 sulfur.
				new ItemStack(ModItems.sulfur), 0.50f  		// 50% chance to get an extra one.
				));
		RecipeManagers.fractionatorManager.addRecipeWithOptionalFluidBonus(300, new ItemStack(Blocks.netherrack), new FluidStack(FluidRegistry.LAVA, 10), ImmutableMap.of(
				new ItemStack(ModItems.sulfur), 1f, 		// 100% chance to get sulfur.
				new ItemStack(ModItems.sulfur), 0.75f,		// 75% chance to get an extra one.
				new ItemStack(Items.gunpowder), 0.05f  		// 5% chance to get gunpowder.
				));
		RecipeManagers.dryDistillerManager.addRecipe(new ItemStack(Items.coal, 1, 1), ImmutableMap.of(
				new ItemStack(ModItems.tar), 1f, 			// 100% chance to get tar.
				new ItemStack(ModItems.resin), 0.90f  		// 90% chance to get resin.
				));
		RecipeManagers.dryDistillerManager.addRecipe(new ItemStack(Items.gunpowder), ImmutableMap.of(
				new ItemStack(ModItems.sulfur), 0.95f 		// 95% chance to get sulfur.
				));
		RecipeManagers.fractionatorManager.addRecipeWithOptionalFluidBonus(new ItemStack(ModItems.resin), new FluidStack(ModFluids.creosote, 500), ImmutableMap.of(
				new ItemStack(ModItems.refinedRubber), 1f 	// 100% chance to get refined rubber.
				));
		RecipeManagers.fractionatorManager.addRecipe(new ItemStack(ModItems.tar), new FluidStack(ModFluids.turpentine, 500), ImmutableMap.of(
				new ItemStack(ModItems.pitch), 1f 			// 100% chance to get pitch.
				));
		RecipeManagers.fractionatorManager.addRecipe(new ItemStack(Items.potato), new FluidStack(ModFluids.ethanol, 250), ImmutableMap.of());
		RecipeManagers.fractionatorManager.addRecipe(new ItemStack(Items.bone), new FluidStack(ModFluids.kerosene, 100), ImmutableMap.of());
		RecipeManagers.fractionatorManager.addRecipe(new ItemStack(Blocks.log), new FluidStack(ModFluids.methanol, 100), ImmutableMap.of());
	}
	
	private static void registerCrafting() {
		GameRegistry.addRecipe(new ItemStack(ModItems.roadWheel, 2), new Object[] {
				" R ", "RWR", " R ", 'R', ModItems.refinedRubber, 'W', Blocks.planks
		});
		// GameRegistry.addShapedRecipe(new
		// ItemStack(ModBlocks.dryDistillerIdle), new Object[] {"SIS","I
		// I","SNS", 'S', Blocks.stone, 'I', Items.iron_ingot, 'N',
		// Blocks.netherrack);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.dryDistillerIdle), new Object[] {
				"SIS", "IFI", "SNS", 'S', Blocks.stone, 'I', Items.iron_ingot, 'N', Blocks.netherrack, 'F', Blocks.furnace
		});
		// GameRegistry.addShapedRecipe(new
		// ItemStack(ModBlocks.dryDistillerIdle), new Object[]
		// {"SHS","IFI","SNS", 'S', Blocks.stone, 'I', Items.iron_ingot, 'N',
		// Blocks.netherrack, 'F', Blocks.furnace, 'H', Blocks.hopper});
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.roadBlock, 6), new Object[] {
				"SPS", "GPG", "CPC", 'S', Blocks.sand, 'P', ModItems.tar, 'G', Blocks.gravel, 'C', Blocks.cobblestone
		});
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.trafficDirector), new Object[] {
				"III", "ISI", " T ", 'I', Items.iron_ingot, 'S', Items.sign, 'T', Items.stick
		});
//		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.crate), new Object[] {
//				"LPL", "PRP", "LPL", 'L', Blocks.log, 'P', Blocks.planks, 'R', ModItems.resin
//		});
		
		//TODO: placeholder recipe for fractionator
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.fractionatorIdle), ModItems.pitch, ModBlocks.dryDistillerIdle, ModItems.sulfur);
	}

	private static void registerShapelessCrafting() {
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.handbook), Items.book, ModItems.shippingOrder);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.shippingOrder, 4), Items.paper, new ItemStack(Items.dye, 1, 0), ModItems.sulfur);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.rubberBlock), ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber, ModItems.refinedRubber);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.refinedRubber, 8), new ItemStack(ModBlocks.rubberBlock));
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.roadBlock), ModItems.tar, Blocks.cobblestone, Blocks.gravel, Blocks.sand);
	}
	
	private static void registerOreDictCrafting() {
		GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.crate, new Object[] {
				"LPL", "PRP", "LPL", 'L', Blocks.log, 'P', Blocks.planks, 'R', ModItems.resin
		}));
	}

	private static void registerFurnaceCrafting() {
		GameRegistry.addSmelting(new ItemStack(Items.egg), new ItemStack(ModItems.sulfur), 0.8f);
	}


}
