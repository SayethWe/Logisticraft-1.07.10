package com.sinesection.logisticraft.proxy;

import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.crafting.LogisticraftDryDistillerCrafting;
import com.sinesection.logisticraft.crafting.LogisticraftVanillaCrafting;
import com.sinesection.logisticraft.network.LogisticraftGuiHandler;
import com.sinesection.logisticraft.registrars.ModBlocks;
import com.sinesection.logisticraft.registrars.ModItems;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		ModItems.loadItems();
		ModItems.registerItems();
		ModBlocks.loadBlocks();
		ModBlocks.registerBlocks();
		
		
	}

	public void init(FMLInitializationEvent e) {
		LogisticraftVanillaCrafting.registerCrafting();
		LogisticraftDryDistillerCrafting.registerCrafting();
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new LogisticraftGuiHandler());
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

}
