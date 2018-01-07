package com.sinesection.geekman9097.logisticraft.block;

import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ModBlocks {
	
	public static Item creativeTabIconItem;
	
	public static final LogisticraftBlock dryDistiller = new BlockDryDistiller();
	public static final LogisticraftBlock rubberBlock = new BlockRubber();
	
	public static final Set<LogisticraftBlock> blocks = new HashSet<>();

	public static void registerBlocks() {
		for (LogisticraftBlock block : blocks) {
			GameRegistry.registerBlock(block, block.getRegistryName());
		}
	}
	
	public static void loadBlocks() {
		creativeTabIconItem = new ItemBlock(dryDistiller);
		blocks.add(dryDistiller);
		blocks.add(rubberBlock);
	}
	
}
