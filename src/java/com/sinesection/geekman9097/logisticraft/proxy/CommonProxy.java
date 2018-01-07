package com.sinesection.geekman9097.logisticraft.proxy;

import com.sinesection.geekman9097.logisticraft.crafting.LogisticraftVanillaCrafting;
import com.sinesection.geekman9097.logisticraft.item.ModItems;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		ModItems.loadItems();
		ModItems.registerItems();
	}

	public void init(FMLInitializationEvent e) {
		LogisticraftVanillaCrafting.registerCrafting();
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

}
