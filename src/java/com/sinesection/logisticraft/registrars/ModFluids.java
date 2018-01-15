package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.fluid.LogisticraftFluid;

import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

	public static final LogisticraftFluid creosote = new LogisticraftFluid("creosote");
	public static final LogisticraftFluid turpentine = new LogisticraftFluid("turpentine");
	public static final LogisticraftFluid lowGradeFuel = new LogisticraftFluid("gasoline");
	public static final LogisticraftFluid midGradeFuel = new LogisticraftFluid("diesel");
	public static final LogisticraftFluid highGradeFuel = new LogisticraftFluid("Kerothanol"); //kerosene/methanol mix
	
	public static final Set<LogisticraftFluid> fluids = new HashSet<>();
	
	public static void registerFluids() {
		for(LogisticraftFluid fluid : fluids) {
			FluidRegistry.registerFluid(fluid);
		}
	}
	
	public static void loadFluids() {
		loadFluid(creosote);
		loadFluid(turpentine);
	}
	
	private static void loadFluid(LogisticraftFluid fl) {
		fluids.add(fl);
	}
	
}
