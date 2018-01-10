package com.sinesection.logisticraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class BlockRubber extends LogisticraftBlock {

	public BlockRubber() {
		super("blockRubber", Material.cloth);
	}

	@Override
	public void onFallenUpon(World world, int x, int y, int z, Entity entity, float distance) {
		entity.fallDistance = 0;
		entity.addVelocity(0, 2, 0);
		super.onFallenUpon(world, x, y, z, entity, distance);
	}
}
