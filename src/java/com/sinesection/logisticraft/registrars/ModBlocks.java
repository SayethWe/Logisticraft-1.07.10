package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.block.*;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ModBlocks {
	
	public static Item creativeTabIconItem;
	
	public static final Block dryDistillerIdle = new BlockDryDistiller("dryDistillerIdle", false);
	public static final Block dryDistillerActive = new BlockDryDistiller("dryDistillerActive", true).setLightLevel(3f);
	public static final LogisticraftBlock rubberBlock = new BlockRubber();
	public static final LogisticraftBlock roadBlock = new BlockRoadway(0);
	
	public static final Set<Block> blocks = new HashSet<>();

	public static void registerBlocks() {
		for (Block block : blocks) {
			if(block instanceof LogisticraftBlock)
				GameRegistry.registerBlock(block, ((LogisticraftBlock) block).getRegistryName());
			else
				GameRegistry.registerBlock(block, block.getUnlocalizedName());
		}
	}
	
	public static void loadBlocks() {
		creativeTabIconItem = new ItemBlock(dryDistillerIdle);
		blocks.add(dryDistillerIdle);
		blocks.add(dryDistillerActive);
		blocks.add(rubberBlock);
		blocks.add(roadBlock);
		for(int i = 1; i <= 3; i++) {
			blocks.add(new BlockRoadway(i));
		}
	}
	
}
