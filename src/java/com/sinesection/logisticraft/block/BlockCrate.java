package com.sinesection.logisticraft.block;

import java.util.Random;

import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.block.tileentity.TileEntityCrate;
import com.sinesection.logisticraft.block.tileentity.TileEntityDryDistiller;
import com.sinesection.logisticraft.network.LogisticraftGuiHandler;
import com.sinesection.logisticraft.registrars.ModBlocks;
import com.sinesection.utils.Log;
import com.sinesection.utils.LogisticraftUtils;

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
		this.setHardness(2f);
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
		if(itemStack.hasTagCompound()) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			LogisticraftUtils.addLocationToNBT(nbt, x, y, z);//to stop it throwing a fit about missing coord tags.
			world.getTileEntity(x, y, z).readFromNBT(nbt);
			LogisticraftUtils.getLogger().info("writing NBT" + nbt);
		}
	}
	
	

	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			FMLNetworkHandler.openGui(player, Main.instance, LogisticraftGuiHandler.guiIdCrate, world, x, y, z);
			LogisticraftUtils.getLogger().info("Opening Crate GUI at "+ x+","+y+","+z);
		}
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMeta) {
		float xOff = this.rand.nextFloat() * 0.8f + 0.1f;
		float yOff = this.rand.nextFloat() * 0.8f + 0.1f;
		float zOff = this.rand.nextFloat() * 0.8f + 0.1f;
		
		TileEntityCrate tileEntity = (TileEntityCrate) world.getTileEntity(x, y, z);
		
		ItemStack item = new ItemStack(Item.getItemFromBlock(ModBlocks.crate));
		if(tileEntity != null) {
			NBTTagCompound data = new NBTTagCompound();
			tileEntity.writeToNBT(data);
			LogisticraftUtils.removeLocationFromNBT(data); //To make it not location-specific
			item.setTagCompound(data);
		}
		EntityItem blockDrop = new EntityItem(world, (double) ((float)x + xOff), (double) ((float)y + yOff), (double) ((float)z + zOff), item);
		world.spawnEntityInWorld(blockDrop);
		
		world.func_147453_f(x, y, z, oldBlock); // Pretty sure this sends block updates
		world.markBlockForUpdate(x, y, z);
		
		super.breakBlock(world, x, y, z, oldBlock, oldMeta);
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

}
