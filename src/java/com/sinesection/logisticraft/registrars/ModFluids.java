package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.fluid.LogisticraftBlockFluid;
import com.sinesection.logisticraft.fluid.LogisticraftFluid;
import com.sinesection.logisticraft.fluid.LogisticraftFluidHandler;
import com.sinesection.logisticraft.item.ItemLogisticraftBucket;
import com.sinesection.logisticraft.item.renderers.LogisticraftItemBucketRenderer;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ModFluids {

	public static final LogisticraftFluid creosote = new LogisticraftFluid("creosote").setColor(0x775555).setFuel();
	public static final LogisticraftFluid methanol = new LogisticraftFluid("methanol").setFuel();
	public static final LogisticraftFluid ethanol = new LogisticraftFluid("ethanol").setFlammability(150).setColor(0xf0c309).setTextureNames("water_still", "water_flow");
	public static final LogisticraftFluid turpentine = new LogisticraftFluid("turpentine").setTextureNames("water_still", "water_flow").setColor(0x999999, true).setFuel();
	public static final LogisticraftFluid kerosene = new LogisticraftFluid("kerosene").setTextureNames("water_still", "water_flow").setColor(0x999999, true).setFuel();
	public static final LogisticraftFluid lowGradeFuel = new LogisticraftFluid("gasoline").setTextureNames("water_still", "water_flow").setColor(0xEEEEEE, true).setFuel();
	public static final LogisticraftFluid midGradeFuel = new LogisticraftFluid("diesel").setTextureNames("water_still", "water_flow").setColor(0xAAAAAA, true).setFuel();
	public static final LogisticraftFluid highGradeFuel = new LogisticraftFluid("kerothanol").setTextureNames("water_still", "water_flow").setColor(0x8888AA, true).setFuel(); //kerosene/ethanol mix
	
	public static final Set<LogisticraftFluid> fluids = new HashSet<LogisticraftFluid>();
	
	private static LogisticraftItemBucketRenderer bucketRender;
	
	
	public static void registerFluids() {
		bucketRender = new LogisticraftItemBucketRenderer();
		for(LogisticraftFluid fluid : fluids) {
			FluidRegistry.registerFluid(fluid);
			LogisticraftBlockFluid fluidBlock = new LogisticraftBlockFluid(fluid).setCreativeTab(Main.tabLogisticraftBlocks);
			GameRegistry.registerBlock(fluidBlock, fluidBlock.getRegistryName());
			fluid.setBlock(fluidBlock);
			ItemLogisticraftBucket itemBucket = new ItemLogisticraftBucket(fluid);
			GameRegistry.registerItem(itemBucket, itemBucket.getUnlocalizedName());
			MinecraftForgeClient.registerItemRenderer(itemBucket, bucketRender);
			LogisticraftFluidHandler.registerLogisticraftFluid(fluidBlock, itemBucket);
			FluidContainerRegistry.registerFluidContainer(new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucket));
		}
	}
	
	public static void loadFluids() {
		loadFluid(creosote);
		loadFluid(ethanol);
		loadFluid(kerosene);
		loadFluid(turpentine);
		loadFluid(methanol);
		loadFluid(lowGradeFuel);
		loadFluid(midGradeFuel);
		loadFluid(highGradeFuel);
	}
	
	private static void loadFluid(LogisticraftFluid fl) {
		fluids.add(fl);
	}
	
}
