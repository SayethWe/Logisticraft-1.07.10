package com.sinesection.logisticraft.registrars;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


import com.sinesection.logisticraft.block.*;

import com.sinesection.logisticraft.render.LogisticraftResource;
import com.sinesection.utils.Log;
import com.sinesection.utils.LogisticraftUtils;


import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;


public class ModBlocks {

	public static final Block dryDistillerIdle = new BlockDryDistiller(false);
	public static final Block dryDistillerActive = new BlockDryDistiller(true).setLightLevel(6f);
	public static final Block fractionatorIdle = new BlockFractionator(false);
	public static final Block fractionatorActive = new BlockFractionator(true).setLightLevel(6f);
	public static final Block mixerIdle = new BlockMixer(false);
	public static final Block mixerActive = new BlockMixer(true).setLightLevel(6f);
	public static final Block rubberBlock = new BlockRubber();

	public static final Block roadBlock = new BlockRoadway();

	public static final Set<Block> blocks = new HashSet<>();

	public static void registerBlocks() {
		for (Block block : blocks) {
			if (block instanceof LogisticraftBlock)
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
		
		blocks.add(roadBlock);
	}

	@SideOnly(Side.CLIENT)
	public static void loadTexMaps() {
		try {
			BufferedImage[] topImages = LogisticraftUtils.splitImage(BlockRoadway.TEXTURE_ROWS, BlockRoadway.TEXTURE_COLS, "/assets/logisticraft/textures/blocks/roadway_top.png");
			for (int i = 0; i < topImages.length; i++) {
				Log.info("Loading road texture " + i + ", null=" + (topImages[i] == null));
				if (topImages[i] == null)
					continue;
				Minecraft.getMinecraft().getTextureManager().loadTexture(new LogisticraftResource("roadway_" + i), new DynamicTexture(topImages[i]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
