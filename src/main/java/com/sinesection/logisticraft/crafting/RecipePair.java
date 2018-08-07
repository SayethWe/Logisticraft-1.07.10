package com.sinesection.logisticraft.crafting;

import java.util.List;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.api.crafting.ILogisticraftRecipe;
import com.sinesection.utils.InventoryUtil;

public class RecipePair<R extends ILogisticraftRecipe> {

	public static final RecipePair<ILogisticraftRecipe> EMPTY = new RecipePair<ILogisticraftRecipe>(null, null);

	@Nullable
	private final R recipe;
	private final List<String> oreDictEntries;

	public RecipePair(R recipe, String[][] oreDictEntries) {
		this.recipe = recipe;
		this.oreDictEntries = InventoryUtil.getOreDictAsList(oreDictEntries);
	}

	public boolean isEmpty() {
		return recipe == null;
	}

	public R getRecipe() {
		return recipe;
	}

	public List<String> getOreDictEntries() {
		return oreDictEntries;
	}
}
