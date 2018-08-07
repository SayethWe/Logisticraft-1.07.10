package com.sinesection.logisticraft.block;

import javax.annotation.Nullable;

import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IBlockRoadway {

	boolean canConnectTo(World world, int x, int y, int z, ForgeDirection side);

	IIcon getTopIconFromMeta(int meta);

	int encodeMeta(@Nullable boolean[] connections);

	boolean[] decodeMeta(int meta);
}
