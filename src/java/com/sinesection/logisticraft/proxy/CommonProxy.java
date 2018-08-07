package com.sinesection.logisticraft.proxy;

import com.sinesection.logisticraft.Logisticraft;
import com.sinesection.logisticraft.crafting.LogisticraftDryDistillerCrafting;
import com.sinesection.logisticraft.crafting.LogisticraftVanillaCrafting;
import com.sinesection.logisticraft.fluid.LogisticraftFluidHandler;
import com.sinesection.logisticraft.network.LogisticraftGuiHandler;
import com.sinesection.logisticraft.network.LogisticraftNetwork;
import com.sinesection.logisticraft.registrars.ModBlocks;
import com.sinesection.logisticraft.registrars.ModFluids;
import com.sinesection.logisticraft.registrars.ModItems;
import com.sinesection.logisticraft.registrars.ModTileEntities;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		LogisticraftNetwork.registerChannels();
		ModItems.loadItems();
		ModItems.registerItems();
		ModBlocks.loadBlocks();
		ModBlocks.registerBlocks();
		ModTileEntities.loadTileEntities();
		ModTileEntities.registerTileEntities();
		ModFluids.loadFluids();
		ModFluids.registerFluids();
	}

	public void init(FMLInitializationEvent e) {
		LogisticraftVanillaCrafting.registerCrafting();
		LogisticraftVanillaCrafting.registerFurnaceCrafting();
		LogisticraftVanillaCrafting.registerShapelessCrafting();
		LogisticraftVanillaCrafting.registerOreDictCrafting();
		LogisticraftDryDistillerCrafting.loadRecipes();
		LogisticraftDryDistillerCrafting.registerCrafting();
		NetworkRegistry.INSTANCE.registerGuiHandler(Logisticraft.instance, new LogisticraftGuiHandler());
		MinecraftForge.EVENT_BUS.register(new LogisticraftFluidHandler());
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

}
