package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.block.BlockDryDistiller;
import com.sinesection.logisticraft.block.BlockRubber;
import com.sinesection.logisticraft.block.LogisticraftBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ModBlocks {
	
	public static Item creativeTabIconItem;
	
	public static final Block dryDistillerIdle = new BlockDryDistiller("dryDistillerIdle", false);
	public static final Block dryDistillerActive = new BlockDryDistiller("dryDistillerActive", true).setLightLevel(3f);
	public static final LogisticraftBlock rubberBlock = new BlockRubber();
	
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
		creativeTabIconItem = Item.getItemFromBlock(dryDistillerIdle);
		blocks.add(dryDistillerIdle);
		blocks.add(dryDistillerActive);
		blocks.add(rubberBlock);
	}
	
}
