package com.sinesection.logisticraft.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MixerCraftingRecipe {

	private static final int MAX_FLUIDS_IN = 4;
	
	public final FluidStack[] fluidInputs;
	public final ItemStack itemInput;
	public final FluidStack fluidOutput;
	public final ItemStack itemOutput;
	public final boolean requiresRefinery;
	public final int minTemp;
	
	public MixerCraftingRecipe(FluidStack fluidOutput, ItemStack itemOutput, boolean requiresRefinery, int requiredTemp, ItemStack itemInput, FluidStack... fluidInputs) {
		if (fluidOutput == null && itemOutput == null) throw new IllegalArgumentException("Must output Something");
		if(fluidInputs.length > MAX_FLUIDS_IN || fluidInputs.length < 0)
			throw new IllegalArgumentException("Illegal number of fluids in : " + fluidInputs + " Min: 0, Max: " + MAX_FLUIDS_IN);
		
		this.itemInput = itemInput;
		this.fluidOutput = fluidOutput;
		this.itemOutput = itemOutput;
		this.fluidInputs = fluidInputs;
		this.requiresRefinery = requiresRefinery;
		this.minTemp = requiredTemp;
	}

}
