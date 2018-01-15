package com.sinesection.logisticraft.proxy;

import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.crafting.LogisticraftDryDistillerCrafting;
import com.sinesection.logisticraft.crafting.LogisticraftVanillaCrafting;
import com.sinesection.logisticraft.network.LogisticraftGuiHandler;
import com.sinesection.logisticraft.registrars.ModBlocks;
import com.sinesection.logisticraft.registrars.ModItems;
import com.sinesection.logisticraft.registrars.ModTileEntities;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraftforge.fluids.FluidRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		ModItems.loadItems();
		ModItems.registerItems();
		ModBlocks.loadBlocks();
		ModBlocks.registerBlocks();
		ModTileEntities.loadTileEntities();
		ModTileEntities.registerTileEntities();
	}

	public void init(FMLInitializationEvent e) {
		LogisticraftVanillaCrafting.registerCrafting();
		LogisticraftVanillaCrafting.registerFurnaceCrafting();
		LogisticraftVanillaCrafting.registerShapelessCrafting();
		LogisticraftDryDistillerCrafting.loadRecipes();
		LogisticraftDryDistillerCrafting.registerCrafting();
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new LogisticraftGuiHandler());
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

}
