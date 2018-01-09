package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.block.tileentity.LogisticraftTileEntity;
import com.sinesection.logisticraft.crafting.DryDistillerCraftingRecipe;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileEntities {

	private static final Set<Class<LogisticraftTileEntity>> tileEnts = new HashSet<>();
	
	public static void registerTileEntities() {
		for(Class<LogisticraftTileEntity> tileEnt : tileEnts) {
			GameRegistry.registerTileEntity(tileEnt, tileEnt);
		}
	}
	
	public static void loadTileEntites() {
		
	}
}
