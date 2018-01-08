package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.block.TileEntity.LogisticraftTileEntity;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileEntities {

	private static final Set<LogisticraftTileEntity> tileEnts = new HashSet<>();
	
	public static void registerTileEntities() {
		for(LogisticraftTileEntity tileEnt : tileEnts) {
			GameRegistry.registerTileEntity(tileEnt.getClass(), tileEnt.getRegistryName());
		}
	}
	
	public static void loadTileEntites() {
		
	}
}
