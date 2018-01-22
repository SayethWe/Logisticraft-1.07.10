package com.sinesection.logisticraft.crafting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sinesection.logisticraft.registrars.ModFluids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class LogisticraftMixerCrafting {

	private static final Set<MixerCraftingRecipe> toRegister = new HashSet<>();
	private static final Map<List<FluidStack>,Map<ItemStack,MixerCraftingRecipe>> mixerCraftingRecipes = new HashMap<>();
	//TODO: figure out implementation
	
	public static void registerCrafting() {
		for(MixerCraftingRecipe mcr : toRegister) {
			registerRecipe(mcr);
		}
	}
	
	private static void registerRecipe(MixerCraftingRecipe recipe) {
		// TODO Auto-generated method stub
		if (recipe == null) throw new IllegalArgumentException("Can't register a null recipe!");
		
	}

	public static void loadRecipes() {
		//Note: If you want to have an item-> item, use two fluidstacks of water for input, as cooling.
		loadRecipe(new MixerCraftingRecipe(new FluidStack(ModFluids.highGradeFuel, 1000), null, false, 2250, null, new FluidStack(ModFluids.ethanol, 500), new FluidStack(ModFluids.kerosene, 500)));
	}
	
	private static void loadRecipe(MixerCraftingRecipe mcr) {
		toRegister.add(mcr);
	}
	
}
