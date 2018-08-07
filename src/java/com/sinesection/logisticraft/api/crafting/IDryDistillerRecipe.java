package com.sinesection.logisticraft.api.crafting;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;

public interface IDryDistillerRecipe {

	/**
	 * @return Number of work cycles required to craft the recipe once.
	 */
	int getProcessTime();

	/**
	 * The item for this recipe to match against.
	 **/
	ItemStack getInput();

	/**
	 * Returns a list of all possible products and their estimated probabilities
	 * (0.0 to 1.0], to help mods that display recipes
	 **/
	Map<ItemStack, Float> getAllProducts();

	/**
	 * Returns the randomized products from processing one input item.
	 **/
	List<ItemStack> getProducts(Random random);
}
