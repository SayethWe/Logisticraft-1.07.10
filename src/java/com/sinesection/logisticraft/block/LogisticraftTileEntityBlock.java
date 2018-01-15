package com.sinesection.logisticraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class LogisticraftTileEntityBlock extends LogisticraftBlock implements ITileEntityProvider {

	public LogisticraftTileEntityBlock(String name, Material mat) {
		super(name, mat);
		this.isBlockContainer = true;
	}
	
	@Override
	public abstract TileEntity createNewTileEntity(World w, int meta);
	//just because the other naming convention is ugly,
	//and this keeps me from having to change it each time
	
	@Override
	public void breakBlock(World w, int x, int y, int z, Block b, int meta) {
		super.breakBlock(w, x, y, z, b, meta);
		w.removeTileEntity(x, y, z);
	}

	@Override
	public boolean onBlockEventReceived(World w, int x, int y, int z, int eventId, int eventParam) {
		super.onBlockEventReceived(w, x, y, z, eventId, eventParam);
		TileEntity tileEnt = w.getTileEntity(x, y, z);
		return  tileEnt == null ? false : tileEnt.receiveClientEvent(eventId, eventParam);
	}

}
