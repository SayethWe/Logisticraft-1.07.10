package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.fluid.LogisticraftBlockFluid;
import com.sinesection.logisticraft.fluid.LogisticraftFluid;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

	public static final LogisticraftFluid creosote = new LogisticraftFluid("creosote").setColor(0x775555);
	public static final LogisticraftFluid turpentine = new LogisticraftFluid("turpentine").setTextureNames("water_still", "water_flow").setColor(0x999999, true);
	public static final LogisticraftFluid lowGradeFuel = new LogisticraftFluid("gasoline").setTextureNames("water_still", "water_flow").setColor(0xEEEEEE, true);
	public static final LogisticraftFluid midGradeFuel = new LogisticraftFluid("diesel").setTextureNames("water_still", "water_flow").setColor(0xAAAAAA, true);
	public static final LogisticraftFluid highGradeFuel = new LogisticraftFluid("kerothanol").setTextureNames("water_still", "water_flow").setColor(0x8888AA, true); //kerosene/methanol mix
	
	public static final Set<LogisticraftFluid> fluids = new HashSet<LogisticraftFluid>();
	
	public static void registerFluids() {
		for(LogisticraftFluid fluid : fluids) {
			FluidRegistry.registerFluid(fluid);
			LogisticraftBlockFluid fluidBlock = new LogisticraftBlockFluid(fluid).setCreativeTab(Main.tabLogisticraftBlocks);
			GameRegistry.registerBlock(fluidBlock, fluidBlock.getRegistryName());
			fluid.setBlock(fluidBlock);
		}
	}
	
	public static void loadFluids() {
		loadFluid(creosote);
		loadFluid(turpentine);
		loadFluid(lowGradeFuel);
		loadFluid(midGradeFuel);
		loadFluid(highGradeFuel);
	}
	
	private static void loadFluid(LogisticraftFluid fl) {
		fluids.add(fl);
	}
	
}
