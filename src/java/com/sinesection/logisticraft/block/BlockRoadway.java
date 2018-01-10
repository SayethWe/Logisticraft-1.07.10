package com.sinesection.logisticraft.block;

import com.sinesection.logisticraft.Main;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockRoadway extends LogisticraftBlock{
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTop, iconBottom;
	
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
	
}
