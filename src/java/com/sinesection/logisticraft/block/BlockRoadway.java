package com.sinesection.logisticraft.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.Constants;
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
import net.minecraftforge.common.util.ForgeDirection;
import scala.actors.threadpool.Arrays;

public class BlockRoadway extends LogisticraftBlock implements IBlockRoadway {

	public static final int TEXTURE_ROWS = 3;
	public static final int TEXTURE_COLS = 4;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	@SideOnly(Side.CLIENT)
	private IIcon[] topIcons;

	public BlockRoadway() {
		super("roadway", Material.rock);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
		int l = MathHelper.floor_double((double) (entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		switch (l) {
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
		this.blockIcon = iIconRegister.registerIcon(Constants.MOD_ID + ":" + "roadway_side");
		this.iconBottom = Blocks.cobblestone.getIcon(1, 0);
		this.topIcons = new IIcon[TEXTURE_ROWS * TEXTURE_COLS];
		for (int i = 0; i < this.topIcons.length; i++) {
			this.topIcons[i] = iIconRegister.registerIcon(Constants.MOD_ID + ":" + "roadway_" + i);
		}
	}

	// 0 - North
	// 1 - East
	// 2 - South
	// 3 - West
	public boolean canConnect(World world, int x, int y, int z, ForgeDirection side) {
		if (side == ForgeDirection.UP || side == ForgeDirection.DOWN)
			return false;
		return world.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ) instanceof IBlockRoadway;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		switch (side) {
		case 0:
			return iconBottom;
		case 1:
			return getTopIconFromMeta(metadata);
		default:
			return this.blockIcon;
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);

		for (int i = 2; i < ForgeDirection.values().length - 1; i++) {
			ForgeDirection dir = ForgeDirection.values()[i];
			world.scheduledUpdatesAreImmediate = true;
			int bx = x + dir.offsetX, by = y + dir.offsetY, bz = z + dir.offsetZ;
			Block b = world.getBlock(bx, by, bz);
			world.scheduleBlockUpdate(bx, by, bz, b, 0);
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		int meta = world.getBlockMetadata(x, y, z);
		boolean[] oldConnections = decodeMeta(meta);
		boolean[] connections = new boolean[4];
		for (int i = 2; i < ForgeDirection.values().length - 1; i++) {
			ForgeDirection dir = ForgeDirection.values()[i];
			connections[i - 2] = canConnectTo(world, x, y, z, dir);
		}
		if (!Arrays.equals(connections, oldConnections))
			world.setBlockMetadataWithNotify(x, y, z, encodeMeta(connections), 2);
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

	@Override
	public IIcon getTopIconFromMeta(int meta) {
		boolean[] connections = decodeMeta(meta);

		return topIcons[0];
	}

	@Override
	public boolean canConnectTo(World world, int x, int y, int z, ForgeDirection side) {
		if (side == ForgeDirection.UP || side == ForgeDirection.DOWN)
			return false;
		return world.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ) instanceof IBlockRoadway;
	}

	// Metadata will be stored like this:
	// cccc00oo
	// c = connection (n,s,w,e)
	// o = orientation (1-4 = n,s,w,e)

	@Override
	public int encodeMeta(@Nullable boolean[] connections) {
		byte consEncoded = 0;
		if (connections != null) {
			if (connections.length == 4) {
				for (int i = 0; i < connections.length; i++) {
					consEncoded = (byte) ((consEncoded << 1) | (connections[i] ? 1 : 0));
				}
			}
		}
		return consEncoded;
	}

	@Override
	public boolean[] decodeMeta(int meta) {
		meta = (meta >> 4) & 15;
		boolean[] connections = new boolean[4];
		for (int i = 0; i < connections.length; i++) {
			boolean flag = (meta & 1) == 1;
			connections[(connections.length - 1) - i] = flag;
			meta >>= 1;
		}
		return connections;
	}
}
