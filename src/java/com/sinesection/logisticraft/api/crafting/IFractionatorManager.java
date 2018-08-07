package com.sinesection.logisticraft.api.crafting;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IFractionatorManager extends ICraftingProvider<IFractionatorRecipe> {
	
	/**
	 * Add a recipe to the fractionator.
	 *
	 * @param input     ItemStack of one item representing the required item for this recipe.
	 * @param products	Crafting results, as a list. For safety, do not make this larger than four different items.
	 */
	void addRecipe(ItemStack input, Map<ItemStack, Float> products);
	
	/**
	 * Add a recipe to the fractionator.
	 *
	 * @param input			ItemStack of one item representing the required item for this recipe.
	 * @param fluidProduct	The fluid that will be created when the input is processed.
	 * @param products		Crafting results, as a list. For safety, do not make this larger than four different items.
	 */
	void addRecipe(ItemStack input, FluidStack fluidProduct, Map<ItemStack, Float> products);
	
	/**
	 * Add a recipe to the fractionator that can also be made in the dry distiller without the fluid output.
	 *
	 * @param input			ItemStack of one item representing the required item for this recipe.
	 * @param fluidProduct	The fluid that will be created when the input is processed.
	 * @param products		Crafting results, as a list. For safety, do not make this larger than four different items.
	 */
	void addRecipeWithOptionalFluidBonus(ItemStack input, FluidStack fluidProduct, Map<ItemStack, Float> products);
	
	/**
	 * Add a recipe to the fractionator.
	 *
	 * @param processTime	Number of work cycles required to craft the recipe once.
	 * @param input			ItemStack of one item representing the required item for this recipe.
	 * @param fluidProduct	The fluid that will be created when the input is processed.
	 * @param products		Crafting results, as a list. For safety, do not make this larger than four different items.
	 */
	void addRecipe(int processTime, ItemStack input, FluidStack fluidProduct, Map<ItemStack, Float> products);
	
	/**
	 * Add a recipe to the fractionator.
	 *
	 * @param processTime	Number of work cycles required to craft the recipe once.
	 * @param input			ItemStack of one item representing the required item for this recipe.
	 * @param products		Crafting results, as a list. For safety, do not make this larger than four different items.
	 */
	void addRecipe(int processTime, ItemStack input, Map<ItemStack, Float> products);
	
	/**
	 * Add a recipe to the fractionator that can also be made in the dry distiller without the fluid output.
	 *
	 * @param processTime	Number of work cycles required to craft the recipe once.
	 * @param input			ItemStack of one item representing the required item for this recipe.
	 * @param fluidProduct	The fluid that will be created when the input is processed.
	 * @param products		Crafting results, as a list. For safety, do not make this larger than four different items.
	 */
	void addRecipeWithOptionalFluidBonus(int processTime, ItemStack input, FluidStack fluidProduct, Map<ItemStack, Float> products);

}
