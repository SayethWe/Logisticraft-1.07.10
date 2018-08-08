package com.sinesection.logisticraft.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class LogisticraftFluidTank extends FluidTank {

	public LogisticraftFluidTank(Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
	}

	public LogisticraftFluidTank(FluidStack fluid, int capacity) {
		super(fluid, capacity);
	}

	public LogisticraftFluidTank(int capacity) {
		super(capacity);
	}

	public LogisticraftFluidTank copy() {
		LogisticraftFluidTank copy = new LogisticraftFluidTank(getFluid(), getCapacity());
		return copy;
	}

}
