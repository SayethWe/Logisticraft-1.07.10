package com.sinesection.logisticraft.crafting;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.api.crafting.IFractionatorManager;
import com.sinesection.logisticraft.api.crafting.IFractionatorRecipe;
import com.sinesection.logisticraft.api.crafting.RecipeManagers;
import com.sinesection.logisticraft.block.tileentity.TileEntityFractionator;
import com.sinesection.utils.ItemStackUtil;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FractionatorRecipeManager implements IFractionatorManager {
	
	private static final Set<IFractionatorRecipe> recipes = new HashSet<>();

	@Override
	public boolean addRecipe(IFractionatorRecipe recipe) {
		return recipes.add(recipe);
	}

	@Override
	public boolean removeRecipe(IFractionatorRecipe recipe) {
		return recipes.remove(recipe);
	}

	@Override
	public Collection<IFractionatorRecipe> recipes() {
		return Collections.unmodifiableSet(recipes);
	}

	@Override
	public void addRecipe(ItemStack input, Map<ItemStack, Float> products) {
		addRecipe(TileEntityFractionator.DEFAULT_PROCESS_SPEED, input, null, products);
	}

	@Override
	public void addRecipe(ItemStack input, FluidStack fluidProduct, Map<ItemStack, Float> products) {
		addRecipe(TileEntityFractionator.DEFAULT_PROCESS_SPEED, input, fluidProduct, products);
	}

	@Override
	public void addRecipeWithOptionalFluidBonus(ItemStack input, FluidStack fluidProduct, Map<ItemStack, Float> products) {
		addRecipeWithOptionalFluidBonus(TileEntityFractionator.DEFAULT_PROCESS_SPEED, input, fluidProduct, products);
	}

	@Override
	public void addRecipe(int processTime, ItemStack input, FluidStack fluidProduct, Map<ItemStack, Float> products) {
		IFractionatorRecipe recipe = new FractionatorRecipe(processTime, input, fluidProduct, products, false);
		addRecipe(recipe);
	}

	@Override
	public void addRecipe(int processTime, ItemStack input, Map<ItemStack, Float> products) {
		addRecipe(processTime, input, null, products);
	}

	@Override
	public void addRecipeWithOptionalFluidBonus(int processTime, ItemStack input, FluidStack fluidProduct, Map<ItemStack, Float> products) {
		IFractionatorRecipe recipe = new FractionatorRecipe(processTime, input, fluidProduct, products, true);
		addRecipe(recipe);
		RecipeManagers.dryDistillerManager.addRecipe(recipe);
	}
	
	@Nullable
	public static IFractionatorRecipe findMatchingRecipe(ItemStack itemStack) {
		if (itemStack == null) {
			return null;
		}

		for (IFractionatorRecipe recipe : recipes) {
			ItemStack recipeInput = recipe.getInput();
			if (ItemStackUtil.isCraftingEquivalent(recipeInput, itemStack)) {
				return recipe;
			}
		}
		return null;
	}

}
