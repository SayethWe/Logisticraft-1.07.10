package com.sinesection.logisticraft.power;

import com.sinesection.logisticraft.block.LogisticraftBlock;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class LogisticraftHeatBlock extends LogisticraftBlock implements IHeatReceiver, IHeatSupplier {

	protected float temp;
	protected float energy;

	protected LogisticraftHeatBlock(String name, String textureName, String registryName, Material mat) {
		super(name, textureName, registryName, mat);
	}

	public LogisticraftHeatBlock(String universalName) {
		this(universalName, universalName, universalName, Material.iron);
	}

	@Override
	public float getVolume() {
		return IHeatReceiver.super.getVolume();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return null;
	}

}
