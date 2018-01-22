package com.sinesection.logisticraft.block;

import java.util.Random;

import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.registrars.ModBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockRoadway extends LogisticraftBlock{

	public static final int NUM_BLOCK_VARIANTS = 4;
	
	private static final int UNIFIED_TEXTURE_ROWS = 5;
	private static final int UNIFIED_TEXTURE_COLS = 4;
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTop, iconBottom;
	
	@SideOnly(Side.CLIENT)
	private IIcon[][] topIcons;
	
	public final int variant;
	
	public BlockRoadway(int variant) {
		super("roadway_" + getVariantString(variant), Material.rock);
		this.variant = variant;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
		int l = MathHelper.floor_double((double) (entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		switch(l) {
		case 0:
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
			break;
		case 1:
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
			break;
		case 2:
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			break;
		case 3:
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
			break;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iIconRegister) {
		this.blockIcon = iIconRegister.registerIcon(Main.MODID + ":" + "roadway_side");
		this.iconTop = iIconRegister.registerIcon(Main.MODID + ":" + "roadway_top_" + getVariantString(variant));
//		BufferedImage[][] topImages = Utils.splitImage(UNIFIED_TEXTURE_ROWS, UNIFIED_TEXTURE_COLS, "error");
//		for(int i = 0; i < UNIFIED_TEXTURE_ROWS; i++) {
//			for(int j = 0; j < UNIFIED_TEXTURE_COLS; j++) {
//				iIconRegister.registerIcon(Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(" ", new DynamicTexture(topImages[i][j])));
//			}
//		}
		 
		this.iconBottom = Blocks.cobblestone.getIcon(1, 0);
	}

	private static String getVariantString(int variantId) {
		switch(variantId) {
		case 0:
			return "straight";
		case 1:
			return "curveRight";
		case 2:
			return "curveLeft";
		case 3:
			return "3intersect";
		case 4:
			return "4intersect";
		default:
			return "straight";
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		switch(side) {
		case 0:
			return iconBottom;
		case 1:
			return iconTop;
		default:
			return this.blockIcon;
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
	}

	private void setDefaultDirection(World world, int x, int y, int z) {
		if (!world.isRemote) {
			Block l = world.getBlock(x, y, z - 1);
			Block il = world.getBlock(x, y, z + 1);
			Block jl = world.getBlock(x - 1, y, z);
			Block kl = world.getBlock(x + 1, y, z);
			byte b0 = 3;
			if (l.isOpaqueCube() && !il.isOpaqueCube()) {
				b0 = 3;
			}
			if (il.isOpaqueCube() && !l.isOpaqueCube()) {
				b0 = 2;
			}
			if (kl.isOpaqueCube() && !jl.isOpaqueCube()) {
				b0 = 5;
			}
			if (jl.isOpaqueCube() && !kl.isOpaqueCube()) {
				b0 = 4;
			}
			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}
	
	@Override
	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(ModBlocks.roadBlock);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(ModBlocks.roadBlock);
	}
	
	public boolean canConnectTo(int side) {
		//TODO
		return false;
	}
}
