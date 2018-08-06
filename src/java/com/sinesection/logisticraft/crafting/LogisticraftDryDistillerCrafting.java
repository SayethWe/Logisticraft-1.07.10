package com.sinesection.logisticraft.crafting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sinesection.logisticraft.registrars.ModBlocks;
import com.sinesection.logisticraft.registrars.ModFluids;
import com.sinesection.logisticraft.registrars.ModItems;
import com.sinesection.utils.LogisticraftUtils;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class LogisticraftDryDistillerCrafting {

	private static final Set<DryDistillerCraftingRecipe> toRegister = new HashSet<>();
	private static final Map<ItemStack, DryDistillerCraftingRecipe> dryDistillerCraftingRecipes = new HashMap<>();

	public static void registerCrafting() {
		for (DryDistillerCraftingRecipe ddcr : toRegister) {
			LogisticraftDryDistillerCrafting.registerRecipe(ddcr);
		}
		LogisticraftUtils.getLogger().info("Registering " + dryDistillerCraftingRecipes.size() + " recipe(s) for " + ModBlocks.dryDistillerIdle.getLocalizedName() + ".");
	}

	public static void loadRecipes() {
		loadRecipe(new DryDistillerCraftingRecipe(new ItemStack(Items.egg), new FluidStack(FluidRegistry.WATER, 50), false, new ItemStack(ModItems.sulfur, 4)));
		loadRecipe(new DryDistillerCraftingRecipe(new ItemStack(Blocks.netherrack), null, false, new ItemStack(ModItems.sulfur, 2)));
		loadRecipe(new DryDistillerCraftingRecipe(new ItemStack(Items.coal, 1, 1), null, false, new ItemStack(ModItems.tar), new ItemStack(ModItems.resin)));
		loadRecipe(new DryDistillerCraftingRecipe(new ItemStack(Items.gunpowder), null, false, new ItemStack(Items.coal, 1, 1), new ItemStack(ModItems.sulfur)));
		loadRecipe(new DryDistillerCraftingRecipe(new ItemStack(ModItems.resin), new FluidStack(ModFluids.creosote, 500), true, new ItemStack(ModItems.refinedRubber)));
		loadRecipe(new DryDistillerCraftingRecipe(new ItemStack(ModItems.tar), new FluidStack(ModFluids.turpentine, 1000), true, new ItemStack(ModItems.pitch)));
		loadRecipe(new DryDistillerCraftingRecipe(new ItemStack(Items.potato), new FluidStack(ModFluids.ethanol, 250), true));
		loadRecipe(new DryDistillerCraftingRecipe(new ItemStack(Items.bone), new FluidStack(ModFluids.kerosene, 1000), true));
		loadRecipe(new DryDistillerCraftingRecipe(new ItemStack(Blocks.log), new FluidStack(ModFluids.methanol, 100), true));
	}

	private static void loadRecipe(DryDistillerCraftingRecipe ddcr) {
		toRegister.add(ddcr);
	}

	public static void registerRecipe(DryDistillerCraftingRecipe recipe) {
		if (recipe == null)
			throw new IllegalArgumentException("Can't register a null recipe!");
		dryDistillerCraftingRecipes.forEach((i, r) -> {
			if (i.isItemEqual(recipe.input))
				throw new IllegalArgumentException("A recipe with the input of '" + recipe.input.getDisplayName() + "' already exists in the LogisticraftDryDistillerCrafting registry!");
		});
		dryDistillerCraftingRecipes.put(recipe.input, recipe);
	}

	public static DryDistillerCraftingRecipe getRecipeFromInput(ItemStack itemStack) {
		for (ItemStack i : dryDistillerCraftingRecipes.keySet()) {
			if (i.isItemEqual(itemStack))
				return dryDistillerCraftingRecipes.get(i);
		}
		return null;
	}

}
