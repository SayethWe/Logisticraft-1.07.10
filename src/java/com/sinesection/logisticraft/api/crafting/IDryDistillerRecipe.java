package com.sinesection.logisticraft.api.crafting;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;

public interface IDryDistillerRecipe extends ILogisticraftRecipe {

	/**
	 * @return Number of work cycles required to craft the recipe once.
	 */
	int getProcessTime();

	/**
	 * @return The item for this recipe to match against.
	 **/
	ItemStack getInput();

	/**
	 * @return A list of all possible products and their estimated probabilities
	 * (0.0 to 1.0], to help mods that display recipes.
	 **/
	Map<ItemStack, Float> getAllProducts();

	/**
	 * @return The randomized products from processing one input item.
	 **/
	List<ItemStack> getProducts(Random random);
	
	/**
	 * @return The unique items made from processing one input item.
	 **/
	List<ItemStack> getUniqueProducts();
}
