package com.sinesection.logisticraft.api.crafting;

import java.util.Map;

import net.minecraft.item.ItemStack;

public interface IDryDistillerManager extends ICraftingProvider<IDryDistillerRecipe> {
	/**
	 * Add a recipe to the dry distiller.
	 *
	 * @param input     ItemStack of one item representing the required item for this recipe.
	 * @param products	Crafting results, as a list. For safety, do not make this larger than four different items.
	 */
	void addRecipe(ItemStack input, Map<ItemStack, Float> products);
	
	/**
	 * Add a recipe to the dry distiller.
	 *
	 * @param processTime	Number of work cycles required to craft the recipe once.
	 * @param input			ItemStack of one item representing the required item for this recipe.
	 * @param products		Crafting results, as a list. For safety, do not this larger than four different items.
	 */
	void addRecipe(int processTime, ItemStack input, Map<ItemStack, Float> products);
}
