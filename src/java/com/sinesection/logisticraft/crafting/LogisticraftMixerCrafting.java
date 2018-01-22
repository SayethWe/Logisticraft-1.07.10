package com.sinesection.logisticraft.crafting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

import com.sinesection.logisticraft.registrars.ModFluids;
import com.sinesection.logisticraft.registrars.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import scala.actors.threadpool.Arrays;

public class LogisticraftMixerCrafting {

	private static final Set<MixerCraftingRecipe> toRegister = new HashSet<>();
	private static final Map<Bag<FluidStack>,Map<ItemStack,MixerCraftingRecipe>> mixerCraftingRecipes = new HashMap<>();
	//TODO: figure out implementation
	
	public static void registerCrafting() {
		for(MixerCraftingRecipe mcr : toRegister) {
			registerRecipe(mcr);
		}
	}
	
	public static MixerCraftingRecipe getRecipefromInput(Bag<FluidStack> fl, ItemStack is) {
		return mixerCraftingRecipes.get(fl).get(is);
	
	}
	private static void registerRecipe(MixerCraftingRecipe recipe) {
		if (recipe == null) throw new IllegalArgumentException("Can't register a null recipe!");
		@SuppressWarnings("unchecked")
		Bag<FluidStack> fluidInputs = new HashBag<>(Arrays.asList(recipe.fluidInputs));
		ItemStack itemInput = recipe.itemInput;
		if(mixerCraftingRecipes.get(fluidInputs) != null) {
			if (mixerCraftingRecipes.get(fluidInputs).get(itemInput) != null) {
				throw new IllegalArgumentException("A recipe with those inputs (Fluid(s): " + fluidInputs + " ,  Item: " + itemInput + ") already exists");
			} else {
				mixerCraftingRecipes.put(fluidInputs, new HashMap<ItemStack, MixerCraftingRecipe>());
			}
		}
		mixerCraftingRecipes.get(fluidInputs).put(itemInput, recipe);
	}

	public static void loadRecipes() {
		//Note: If you want to have an item-> item, use a coolant fluid to avoid errors.
		loadRecipe(new MixerCraftingRecipe(new FluidStack(ModFluids.highGradeFuel, 1000), null, 0, null, true, 1500, 2500, 35e9, null, new FluidStack(ModFluids.ethanol, 500), new FluidStack(ModFluids.kerosene, 500)));
		loadRecipe(new MixerCraftingRecipe(new FluidStack(ModFluids.midGradeFuel, 1000), null, true, 0, 32e6, new ItemStack(ModItems.sulfur), new FluidStack(ModFluids.turpentine, 1000), new FluidStack(ModFluids.turpentine, 1000)));
	}
	
	private static void loadRecipe(MixerCraftingRecipe mcr) {
		toRegister.add(mcr);
	}
	
}
