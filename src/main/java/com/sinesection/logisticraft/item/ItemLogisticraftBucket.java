package com.sinesection.logisticraft.item;

import com.sinesection.logisticraft.Constants;
import com.sinesection.logisticraft.Logisticraft;
import com.sinesection.logisticraft.fluid.LogisticraftFluid;

import net.minecraft.item.ItemBucket;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class ItemLogisticraftBucket extends ItemBucket {

	private final Fluid fluid;

	public ItemLogisticraftBucket(LogisticraftFluid fluid) {
		super(fluid.getBlock());
		this.setUnlocalizedName(fluid.getName() + "_bucket");
		this.setTextureName(Constants.MOD_ID + ":bucketTemplate");
		this.setCreativeTab(Logisticraft.tabLogisticraftItems);
		this.fluid = fluid;
	}

	@Override
	public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
		if (fluid == null)
			return false;
		if (!world.isAirBlock(x, y, z) && world.getBlock(x, y, z).getMaterial().isSolid())
			return false;

		world.setBlock(x, y, z, fluid.getBlock(), 0, 3);
		return true;
	}

	public Fluid getFluid() {
		return fluid;
	}

}
