package com.sinesection.logisticraft.block;

import java.util.Random;

import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.block.tileentity.TileEntityCrate;
import com.sinesection.logisticraft.block.tileentity.TileEntityDryDistiller;
import com.sinesection.logisticraft.network.LogisticraftGuiHandler;
import com.sinesection.logisticraft.registrars.ModBlocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCrate extends LogisticraftTileEntityBlock {

	private Random rand = new Random();
	public BlockCrate() {
		super("crate", Material.wood);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileEntityCrate();
	}
	@Override
	public Item getItemDropped(int par1, Random random, int par3) {
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		// TODO Auto-generated method stub
		return super.createTileEntity(world, metadata);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
	// TODO
		world.getTileEntity(x, y, z).writeToNBT(itemStack.getTagCompound());
	}
	

	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			//FMLNetworkHandler.openGui(player, Main.instance, LogisticraftGuiHandler.guiIdCrate, world, x, y, z);
		}
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMeta) {
		float xOff = this.rand.nextFloat() * 0.8f + 0.1f;
		float yOff = this.rand.nextFloat() * 0.8f + 0.1f;
		float zOff = this.rand.nextFloat() * 0.8f + 0.1f;
		
		TileEntityCrate tileEntity = (TileEntityCrate) world.getTileEntity(x, y, z);
		EntityItem blockDrop = new EntityItem(world, (double) ((float)x + xOff), (double) ((float)y + yOff), (double) ((float)z + zOff), new ItemStack(Item.getItemFromBlock(ModBlocks.crate)));
		if(tileEntity != null) {
			NBTTagCompound data = new NBTTagCompound();
			tileEntity.writeToNBT(data);
			blockDrop.getEntityItem().setTagCompound(data); //TODO: Save NBT
		}
		world.spawnEntityInWorld(blockDrop);
		super.breakBlock(world, x, y, z, oldBlock, oldMeta);
	}

	//TODO: max stack size 1


}
