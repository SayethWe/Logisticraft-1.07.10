package com.sinesection.logisticraft.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.sinesection.logisticraft.api.crafting.IFractionatorRecipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FractionatorRecipe implements IFractionatorRecipe {

	private final int processTime;
	private final ItemStack input;
	private final Map<ItemStack, Float> products;
	@Nullable
	private final FluidStack fluidProduct;
	private final boolean fractionatorOptional;

	public FractionatorRecipe(int processTime, ItemStack input, @Nullable FluidStack fluidProduct, Map<ItemStack, Float> products, boolean fractionatorOptional) {
		Preconditions.checkNotNull(input);
		Preconditions.checkNotNull(products);
		this.processTime = processTime;
		this.input = input;
		this.fluidProduct = fluidProduct;
		this.products = products;
		this.fractionatorOptional = fractionatorOptional;
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
	public boolean canBeMadeInDryDistiller() {
		return fractionatorOptional;
	}

	@Override
	public FluidStack getFluidProduct() {
		return fluidProduct;
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
