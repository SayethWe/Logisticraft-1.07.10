package com.sinesection.logisticraft.crafting;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.api.crafting.IDryDistillerManager;
import com.sinesection.logisticraft.api.crafting.IDryDistillerRecipe;
import com.sinesection.logisticraft.block.tileentity.TileEntityDryDistiller;
import com.sinesection.utils.ItemStackUtil;

import net.minecraft.item.ItemStack;

public class DryDistillerRecipeManager implements IDryDistillerManager {
	
	private static final Set<IDryDistillerRecipe> recipes = new HashSet<>();

	@Override
	public boolean addRecipe(IDryDistillerRecipe recipe) {
		return recipes.add(recipe);
	}

	@Override
	public boolean removeRecipe(IDryDistillerRecipe recipe) {
		return recipes.remove(recipe);
	}

	@Override
	public Collection<IDryDistillerRecipe> recipes() {
		return Collections.unmodifiableSet(recipes);
	}

	@Override
	public void addRecipe(ItemStack input, Map<ItemStack, Float> products) {
		addRecipe(TileEntityDryDistiller.DEFAULT_PROCESS_SPEED, input, products);
	}

	@Override
	public void addRecipe(int processTime, ItemStack input, Map<ItemStack, Float> products) {
		IDryDistillerRecipe recipe = new DryDistillerRecipe(processTime, input, products);
		addRecipe(recipe);
	}
	
	@Nullable
	public static IDryDistillerRecipe findMatchingRecipe(ItemStack itemStack) {
		if (itemStack == null) {
			return null;
		}

		for (IDryDistillerRecipe recipe : recipes) {
			ItemStack recipeInput = recipe.getInput();
			if (ItemStackUtil.isCraftingEquivalent(recipeInput, itemStack)) {
				return recipe;
			}
		}
		return null;
	}

}
