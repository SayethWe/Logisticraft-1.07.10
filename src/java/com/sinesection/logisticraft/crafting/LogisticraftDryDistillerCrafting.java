package com.sinesection.logisticraft.crafting;

import java.util.ArrayList;
import java.util.List;

import com.sinesection.logisticraft.registrars.ModBlocks;
import com.sinesection.logisticraft.registrars.ModItems;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class LogisticraftDryDistillerCrafting {

	private static final List<DryDistillerCraftingRecipe> dryDistillerCraftingRecipies = new ArrayList<DryDistillerCraftingRecipe>();

	public static void registerCrafting() {
		LogisticraftDryDistillerCrafting.registerRecipe(new DryDistillerCraftingRecipe(new ItemStack(Items.redstone, 2), null, false, new ItemStack(ModItems.refinedRubber, 3), new ItemStack(Items.glowstone_dust)));
		System.out.println("Registering " + dryDistillerCraftingRecipies.size() + " recipe(s) for " + ModBlocks.dryDistillerIdle.getLocalizedName() + ".");
	}

	public static void registerRecipe(DryDistillerCraftingRecipe recipe) {
		if (recipe == null)
			throw new IllegalArgumentException("Can't register a null recipe!");
		dryDistillerCraftingRecipies.forEach((r) -> {
			if (r.input.equals(recipe.input))
				throw new IllegalArgumentException("A recipe with the input of '" + recipe.input.getDisplayName() + "' already exists in the LogisticraftDryDistillerCrafting registry!");
		});
		dryDistillerCraftingRecipies.add(recipe);
	}

}
