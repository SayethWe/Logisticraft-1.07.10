package com.sinesection.logisticraft.api.crafting;

import net.minecraftforge.fluids.FluidStack;

public interface IFractionatorRecipe extends IDryDistillerRecipe {

	/**
	 * Allows this recipe to be made in the Dry Distiller, but will not produce
	 * a fluid when made.
	 * 
	 * @return
	 */
	boolean canBeMadeInDryDistiller();

	FluidStack getFluidOutput();

}
