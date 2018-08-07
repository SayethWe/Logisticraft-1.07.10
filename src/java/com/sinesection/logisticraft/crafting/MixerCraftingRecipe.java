package com.sinesection.logisticraft.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MixerCraftingRecipe {

	private static final int MAX_FLUIDS_IN = 4;

	public final FluidStack[] fluidInputs;
	public final ItemStack itemInput;
	public final FluidStack fluidOutput;
	public final ItemStack itemOutput, failOutput;
	public final boolean requiresRefinery, overheatingCausesFail;
	public final float minTemp, maxTemp;
	public final double energy;
	public final float failFluidPercent;

	public MixerCraftingRecipe(FluidStack fluidOutput, ItemStack itemOutput, float failFluidPercent, ItemStack failOutput, boolean requiresRefinery, float requiredTemp, float failTemp, double requiredEnergy, ItemStack itemInput, FluidStack... fluidInputs) {
		if (fluidOutput == null && itemOutput == null)
			throw new IllegalArgumentException("Must output Something");
		if (fluidInputs.length > MAX_FLUIDS_IN || fluidInputs.length < 0)
			throw new IllegalArgumentException("Illegal number of fluids in : " + fluidInputs + " min: 0, Max: " + MAX_FLUIDS_IN);
		if (failOutput != null || failFluidPercent >= 0 && failTemp == -1)
			throw new IllegalArgumentException("Cannot have a failure Item if it cannot fail");
		if (requiredEnergy == 0)
			throw new IllegalArgumentException("Recipes require input Energy amounts.");

		this.itemInput = itemInput;
		this.fluidOutput = fluidOutput;
		this.itemOutput = itemOutput;
		this.fluidInputs = fluidInputs;
		this.requiresRefinery = requiresRefinery;
		this.minTemp = requiredTemp;
		this.maxTemp = failTemp;
		this.energy = requiredEnergy;
		this.overheatingCausesFail = failTemp != -1;
		this.failOutput = failOutput;
		this.failFluidPercent = failFluidPercent;
	}

	public MixerCraftingRecipe(FluidStack fluidOutput, ItemStack itemOutput, boolean requiresRefinery, float requiredTemp, double requiredEnergy, ItemStack itemInput, FluidStack... fluidInputs) {
		this(fluidOutput, itemOutput, -1, null, requiresRefinery, requiredTemp, -1, requiredEnergy, itemInput, fluidInputs);
	}

	public boolean hasLiquidOutput() {
		return fluidOutput != null;
	}

}
