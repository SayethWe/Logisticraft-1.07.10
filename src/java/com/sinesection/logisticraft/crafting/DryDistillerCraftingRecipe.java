package com.sinesection.logisticraft.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class DryDistillerCraftingRecipe {
	
	public final ItemStack input;
	public final ItemStack[] outputs;
	public final FluidStack fluidOutput;
	public final boolean fractionatorRequired;
	
	/**
	 * Makes a simple recipe that can be made in the Dry Distiller OR Fractionator.
	 * @param input
	 * @param outputs
	 */
	public DryDistillerCraftingRecipe(ItemStack input, ItemStack ...outputs) {
		this(input, null, false, outputs);
	}
	
	/**
	 * Makes a simple recipe that can ONLY be made in the Fractionator.
	 * @param input
	 * @param fractionatorRequired
	 * @param outputs
	 */
	public DryDistillerCraftingRecipe(ItemStack input, boolean fractionatorRequired, ItemStack ...outputs) {
		this(input, null, fractionatorRequired, outputs);
	}
	
	/**
	 * Makes a simple recipe that can in the Dry Distiller OR Fractionator, but when made in the latter, it will produce a fluid output.
	 * @param input
	 * @param fluidOutput
	 * @param outputs
	 */
	public DryDistillerCraftingRecipe(ItemStack input, FluidStack fluidOutput, ItemStack ...outputs) {
		this(input, fluidOutput, false, outputs);
	}
	
	/**
	 * Makes a simple recipe that can ONLY be made in the Fractionator, and it will produce a fluid output.
	 * @param input
	 * @param fluidOutput
	 * @param fractionatorRequired
	 * @param outputs
	 */
	public DryDistillerCraftingRecipe(ItemStack input, FluidStack fluidOutput, boolean fractionatorRequired, ItemStack ...outputs) {
		if(input == null) 
			throw new IllegalArgumentException("Input ItemStack cannot be null!");
		if(outputs.length > 4)
			throw new IllegalArgumentException("Amout of output ItemStacks is illegal: " + outputs.length + ", Max: 4");
		
		this.input = input;
		this.outputs = outputs;
		this.fluidOutput = fluidOutput;
		this.fractionatorRequired = fractionatorRequired;
	}

	public boolean hasLiquidOutput() {
		return this.fluidOutput != null && this.fluidOutput.amount > 0;
	}
	
	

}
