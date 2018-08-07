package com.sinesection.logisticraft.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.sinesection.logisticraft.api.crafting.IDryDistillerRecipe;

import net.minecraft.item.ItemStack;

public class DryDistillerRecipe implements IDryDistillerRecipe {
	
	private final int processTime;
	private final ItemStack input;
	private final Map<ItemStack, Float> products;
	
	public DryDistillerRecipe(int processTime, ItemStack input, Map<ItemStack, Float> products) {
		Preconditions.checkNotNull(input);
		Preconditions.checkNotNull(products);
		this.processTime = processTime;
		this.input = input;
		this.products = products;
	}

	@Override
	public int getProcessTime() {
		return processTime;
	}

	@Override
	public ItemStack getInput() {
		return input;
	}

	@Override
	public Map<ItemStack, Float> getAllProducts() {
		return ImmutableMap.copyOf(products);
	}

	@Override
	public List<ItemStack> getProducts(Random random) {
		List<ItemStack> products = new ArrayList<ItemStack>();

		for (Map.Entry<ItemStack, Float> entry : this.products.entrySet()) {
			float probability = entry.getValue();

			if (probability >= 1.0) {
				products.add(entry.getKey().copy());
			} else if (random.nextFloat() < probability) {
				products.add(entry.getKey().copy());
			}
		}

		return products;
	}
	
	@Override
	public List<ItemStack> getUniqueProducts() {
		Set<ItemStack> products = getAllProducts().keySet();
		List<ItemStack> uniques = new ArrayList<>();

		for (ItemStack product : products) {
			uniques.forEach((listStack) -> {
				if (listStack.isItemEqual(product)) {
					uniques.add(new ItemStack(product.getItem(), 1, product.getItemDamage()));
				}
			});
		}

		return uniques;
	}

}
