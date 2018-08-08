package com.sinesection.logisticraft.fluid;

import java.util.List;

import javax.annotation.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public interface ITankManager extends IFluidHandler {
	void containerAdded(Container container, ICrafting crafter);

	void sendTankUpdate(Container container, List<ICrafting> crafters);

	void containerRemoved(Container container);

	IFluidTank getTank(int tankIndex);

	boolean canFillFluidType(FluidStack fluidStack);

	boolean canDrainFluidType(FluidStack fluidStack);

	/**
	 * For updating tanks on the client
	 */
	@SideOnly(Side.CLIENT)
	void processTankUpdate(int tankIndex, @Nullable FluidStack contents);
}