package com.sinesection.geekman9097.logisticraft.crafting;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class DryDistillerCraftingRecipe {
	
	public final ItemStack input;
	public final ItemStack[] outputs;
	public final FluidStack fluidOutput;
	public final boolean fractionatorRequired;
	
	public DryDistillerCraftingRecipe(ItemStack input, FluidStack fluidOutput, boolean fractionatorRequired, ItemStack ...outputs) {
		if(input == null) 
			throw new IllegalArgumentException("Input ItemStack cannot be null!");
		if(outputs.length > 4 || outputs.length == 0)
			throw new IllegalArgumentException("Amout of output ItemStacks is illegal: " + outputs.length + ", Min: 1, Max: 3");
		if(fluidOutput != null && !fractionatorRequired)
			throw new IllegalArgumentException("Can only have fluid output when the Fractionator is required!");
		
		this.input = input;
		this.outputs = outputs;
		this.fluidOutput = fluidOutput;
		this.fractionatorRequired = fractionatorRequired;
	}

}
