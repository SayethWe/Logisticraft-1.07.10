package com.sinesection.utils;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.Constants;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public abstract class TileUtil {

	public static void registerTile(Class<? extends TileEntity> tileClass, String key){
		GameRegistry.registerTileEntity(tileClass, new ResourceLocation(Constants.MOD_ID, key).getResourcePath());
	}

	public static boolean isUsableByPlayer(EntityPlayer player, TileEntity tile) {
		return !tile.isInvalid() &&
				getTile(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord) == tile &&
				player.getDistanceSq( tile.xCoord + 0.5D,  tile.yCoord + 0.5D,  tile.zCoord + 0.5D) <= 64.0D;
	}

	/**
	 * Returns the tile at the specified position, returns null if it is the wrong type or does not exist.
	 * Avoids creating new tile entities when using a ChunkCache (off the main thread).
	 * see {@link BlockFlowerPot#getActualState(IBlockState, IBlockAccess, BlockPos)}
	 */
	@Nullable
	public static TileEntity getTile(IBlockAccess world, int x, int y, int z) {
		if (world instanceof ChunkCache) {
			ChunkCache chunkCache = (ChunkCache) world;
			return chunkCache.getTileEntity(x, y, z);
		} else {
			return world.getTileEntity(x, y, z);
		}
	}

	/**
	 * Returns the tile of the specified class, returns null if it is the wrong type or does not exist.
	 * Avoids creating new tile entities when using a ChunkCache (off the main thread).
	 * see {@link BlockFlowerPot#getActualState(IBlockState, IBlockAccess, BlockPos)}
	 */
	@Nullable
	public static <T> T getTile(IBlockAccess world, int x, int y, int z, Class<T> tileClass) {
		TileEntity tileEntity = getTile(world, x, y, z);
		if (tileClass.isInstance(tileEntity)) {
			return tileClass.cast(tileEntity);
		} else {
			return null;
		}
	}

	public interface ITileGetResult<T, R>  {
		@Nullable
		R getResult(T tile);
	}

	/**
	 * Performs an {@link ITileGetResult} on a tile if the tile exists.
	 */
	@Nullable
	public static <T, R> R getResultFromTile(IBlockAccess world, int x, int y, int z, Class<T> tileClass, ITileGetResult<T, R> tileGetResult) {
		T tile = getTile(world, x, y, z, tileClass);
		if (tile != null) {
			return tileGetResult.getResult(tile);
		}
		return null;
	}

	public interface ITileAction<T>  {
		void actOnTile(T tile);
	}

	/**
	 * Performs an {@link ITileAction} on a tile if the tile exists.
	 */
	public static <T> void actOnTile(IBlockAccess world, int x, int y, int z, Class<T> tileClass, ITileAction<T> tileAction) {
		T tile = getTile(world, x, y, z, tileClass);
		if (tile != null) {
			tileAction.actOnTile(tile);
		}
	}

	@Nullable
	public static IInventory getInventoryFromTile(@Nullable TileEntity tile, @Nullable EnumFacing side) {
		if (tile == null) {
			return null;
		}


		if (tile instanceof ISidedInventory) {
			return (ISidedInventory) tile;
		}

		if (tile instanceof IInventory) {
			return (IInventory) tile;
		}

		return null;
	}
}