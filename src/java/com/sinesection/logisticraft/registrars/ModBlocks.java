package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.block.*;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class ModBlocks {
	
	public static final Block dryDistillerIdle = new BlockDryDistiller(false);
	public static final Block dryDistillerActive = new BlockDryDistiller(true).setLightLevel(6f);
	public static final Block fractionatorIdle = new BlockFractionator(false);
	public static final Block fractionatorActive = new BlockFractionator(true).setLightLevel(6f);
	public static final Block mixerIdle = new BlockMixer(false);
	public static final Block mixerActive = new BlockMixer(true).setLightLevel(6f);
	public static final Block rubberBlock = new BlockRubber();
	public static final Block roadBlock = new BlockRoadway(0);
	public static final Block[] roadBlockVariants = createRoadVariants();
	public static final Block crate = new BlockCrate();
	
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
		blocks.add(dryDistillerIdle);
		blocks.add(dryDistillerActive);
		blocks.add(fractionatorIdle);
		blocks.add(fractionatorActive);
		blocks.add(fractionatorIdle);
		blocks.add(fractionatorActive);
		blocks.add(mixerIdle);
		blocks.add(mixerActive);
		blocks.add(crate);
		for (Block variant : roadBlockVariants) {
			blocks.add(variant);
		}
	}
	
	private static LogisticraftBlock[] createRoadVariants() {
		LogisticraftBlock[] result = new LogisticraftBlock[BlockRoadway.NUM_BLOCK_VARIANTS];
		for(int i = 0; i < result.length; i++) {
			result[i] = (new BlockRoadway(i+1));
		}
		return result;
	}
	
}
