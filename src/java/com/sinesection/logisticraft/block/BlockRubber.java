package com.sinesection.logisticraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class BlockRubber extends LogisticraftBlock {

	public BlockRubber() {
		super("blockRubber",Material.cloth);
	}

	@Override
	public void onFallenUpon(World p_149746_1_, int p_149746_2_, int p_149746_3_, int p_149746_4_, Entity p_149746_5_,
			float p_149746_6_) {
		p_149746_5_.fallDistance = 0;
		super.onFallenUpon(p_149746_1_, p_149746_2_, p_149746_3_, p_149746_4_, p_149746_5_, p_149746_6_);
	}
	
}
