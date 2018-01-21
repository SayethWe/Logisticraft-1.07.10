package com.sinesection.logisticraft.fluid;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;

public class LogisticraftBlockFluid extends BlockFluidClassic {

	private int flammability;
	private String registryName;
	private IIcon[] blockIcons;
	private boolean isFuel;

	public LogisticraftBlockFluid(LogisticraftFluid fluid) {
		this(fluid, fluid.getName(), fluid.getName());
	}

	public LogisticraftBlockFluid(LogisticraftFluid fluid, String name, String registryName) {
		super(fluid, fluid.getMaterial());
		isFuel = fluid.isFuel;
		this.registryName = registryName;
		this.flammability = fluid.getFlammability();
		setBlockName(name);
		this.blockIcons = new IIcon[2];
	}
	
	public boolean isFlammable() {
		return flammability > 0;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return flammability;
	}

	@Override
	public LogisticraftBlockFluid setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iIconRegister) {
		this.blockIcons[0] = iIconRegister.registerIcon(this.getStillTextureName());
		this.definedFluid.setStillIcon(this.blockIcons[0]);
		this.blockIcon = this.blockIcons[0];
		this.blockIcons[1] = iIconRegister.registerIcon(this.getFlowingTextureName());
		this.definedFluid.setFlowingIcon(this.blockIcons[1]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return (side == 0 || side == 1) ? this.blockIcons[0] : this.blockIcons[1];
	}

	@Override
	public int colorMultiplier(IBlockAccess iBlockAccess, int x, int y, int z) {
		return ((LogisticraftFluid) this.definedFluid).shouldColorAffectTexture() ? this.definedFluid.getColor() : 0xFFFFFF;
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid())
			return false;
		return super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid())
			return false;
		return super.displaceIfPossible(world, x, y, z);
	}

	@Override
	public Item getItemDropped(int meta, Random par2Random, int par3) {
		return null;
	}

	public String getRegistryName() {
		return registryName;
	}

	public IIcon getStillIcon() {
		return this.blockIcons[0];
	}

	public IIcon getFlowingIcon() {
		return this.blockIcons[1];
	}

	public String getStillTextureName() {
		return ((LogisticraftFluid) this.definedFluid).getStillTextureName();
	}

	public String getFlowingTextureName() {
		return ((LogisticraftFluid) this.definedFluid).getFlowingTextureName();
	}

	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return isFuel;
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return isFuel ? 300 : 0;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		// TODO Auto-generated method stub
		return isFuel ? 100 : 0;
	}
	
	
	
	

}
