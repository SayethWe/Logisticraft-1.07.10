package com.sinesection.logisticraft.block;

import com.sinesection.logisticraft.Main;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class BlockRoadway extends LogisticraftBlock{
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTop, iconBottom;
	
	int variant;
	
	public BlockRoadway(int variant) {
		super("roadway_" + getVariantString(variant), Material.rock);
		this.variant = variant;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iIconRegister) {
		this.blockIcon = iIconRegister.registerIcon(Main.MODID + ":" + "roadway_side");
		this.iconTop = iIconRegister.registerIcon(Main.MODID + ":" + "roadway_top_" + getVariantString(variant));
		this.iconBottom = Blocks.cobblestone.getIcon(1, 0);
	}
	
	private static String getVariantString(int variantId) {
		switch(variantId) {
		case 0:
			return "straight";
		case 1:
			return "curve";
		case 2:
			return "3intersect";
		case 3:
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
	
}
