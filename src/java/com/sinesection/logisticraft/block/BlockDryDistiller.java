package com.sinesection.logisticraft.block;

import java.util.Random;

import com.sinesection.logisticraft.Constants;
import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.block.tileentity.TileEntityDryDistiller;
import com.sinesection.logisticraft.network.LogisticraftGuiHandler;
import com.sinesection.logisticraft.registrars.ModBlocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockDryDistiller extends LogisticraftTileEntityBlock {

	private final boolean isActive;

	@SideOnly(Side.CLIENT)
	private IIcon iconFront;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop, iconBottom;

	private static boolean keepInventory;
	
	private Random rand = new Random();

	public BlockDryDistiller(boolean isActive) {
		super("dryDistiller" + (isActive ? "Active" : "Idle"), Material.iron);
		this.isActive = isActive;
		if(isActive)
			this.setCreativeTab(null);
		
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iIconRegister) {
		this.blockIcon = iIconRegister.registerIcon(Constants.MOD_ID + ":" + "distiller_side");
		iconTop = iIconRegister.registerIcon(Constants.MOD_ID + ":" + "distiller_top");
		iconBottom = iIconRegister.registerIcon(Constants.MOD_ID + ":" + "distiller_bottom");
		iconFront = iIconRegister.registerIcon(Constants.MOD_ID + ":" + "distiller_front" + (isActive ? "_active" : ""));
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return metadata == 0 && side == 3 ? iconFront : (side == 1 ? iconTop : (side == 0 ? iconBottom : (side == metadata ? iconFront : this.blockIcon)));
	}

	@Override
	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(ModBlocks.dryDistillerIdle);
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
		int l = MathHelper.floor_double((double) (entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}
		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}
		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}
		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}

		if (itemStack.hasDisplayName()) {
			((TileEntityDryDistiller) world.getTileEntity(x, y, z)).setGuiDisplayName(itemStack.getDisplayName());
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			FMLNetworkHandler.openGui(player, Main.instance, LogisticraftGuiHandler.guiIdDryDistiller, world, x, y, z);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDryDistiller();
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int i) {
		return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		return new ItemStack(ModBlocks.dryDistillerIdle);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(ModBlocks.dryDistillerIdle);
	}

	public static void updateState(boolean active, World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		TileEntityDryDistiller tEntity = (TileEntityDryDistiller) world.getTileEntity(x, y, z);

		keepInventory = true;

		if (active) {
			world.setBlock(x, y, z, ModBlocks.dryDistillerActive);

		} else {
			world.setBlock(x, y, z, ModBlocks.dryDistillerIdle);
		}

		keepInventory = false;

		world.setBlockMetadataWithNotify(x, y, z, meta, 2);

		if (tEntity != null) {
			tEntity.validate();
			world.setTileEntity(x, y, z, tEntity);
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMeta) {
		if(!keepInventory) {
			TileEntityDryDistiller tEntity = (TileEntityDryDistiller) world.getTileEntity(x, y, z);
			
			if(tEntity != null) {
				for(int i = 0; i < tEntity.getSizeInventory(); i++) {
					ItemStack slotContents = tEntity.getStackInSlot(i);
					
					if(slotContents != null) {
						float xOff = this.rand.nextFloat() * 0.8f + 0.1f;
						float yOff = this.rand.nextFloat() * 0.8f + 0.1f;
						float zOff = this.rand.nextFloat() * 0.8f + 0.1f;
						
						while(slotContents.stackSize > 0) {
							int j = this.rand.nextInt(21) + 10;
							
							if(j > slotContents.stackSize)
								j = slotContents.stackSize;
							
							slotContents.stackSize -= j;
							
							EntityItem item = new EntityItem(world, (double) ((float)x + xOff), (double) ((float)y + yOff), (double) ((float)z + zOff), new ItemStack(slotContents.getItem(), j, slotContents.getItemDamage()));
							
							if(slotContents.hasTagCompound())
								item.getEntityItem().setTagCompound((NBTTagCompound) slotContents.getTagCompound().copy());
							
							float speedMult = 0.05f;
							item.motionX = (double) ((float) this.rand.nextGaussian() * speedMult);
							item.motionY = (double) ((float) this.rand.nextGaussian() * speedMult + 0.2f);
							item.motionZ = (double) ((float) this.rand.nextGaussian() * speedMult);
							
							world.spawnEntityInWorld(item);
						}
					}
				}
				
				world.func_147453_f(x, y, z, oldBlock); // Pretty sure this sends block updates
			}
		}
		
		super.breakBlock(world, x, y, z, oldBlock, oldMeta);
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		super.randomDisplayTick(world, x, y, z, random); // TODO Particles and stuff
	}
}
