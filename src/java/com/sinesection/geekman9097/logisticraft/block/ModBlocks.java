package com.sinesection.geekman9097.logisticraft.block;

import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
	
	public static final Set<LogisticraftBlock> blocks = new HashSet<>();

	public static void registerBlocks() {
		for (LogisticraftBlock block : blocks) {
			GameRegistry.registerBlock(block, block.getRegistryName());
		}
	}
	
	public static void loadItems() {
		
	}
	
}
