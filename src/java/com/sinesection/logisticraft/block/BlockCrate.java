package com.sinesection.logisticraft.block;

import com.sinesection.logisticraft.block.tileentity.TileEntityCrate;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCrate extends LogisticraftTileEntityBlock {

	public BlockCrate() {
		super("ShippingCrate", Material.wood);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileEntityCrate();
	}

	//TODO: max stack size 1
	
}
