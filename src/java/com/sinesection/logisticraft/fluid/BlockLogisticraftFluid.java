package com.sinesection.logisticraft.fluid;

import java.util.Random;

import javax.annotation.Nullable;

import org.lwjgl.util.Color;

import com.sinesection.utils.ColorUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockLogisticraftFluid extends BlockFluidClassic {

	private final boolean flammable;
	private final int flammability;
	private final Color color;

	public BlockLogisticraftFluid(Fluid logisticraftFluid) {
		this(logisticraftFluid, 0, false);
	}

	public BlockLogisticraftFluid(Fluid logisticraftFluid, int flammability, boolean flammable) {
		this(logisticraftFluid, flammability, flammable, ColorUtils.fromRGB(logisticraftFluid.getColor()));
	}

	private BlockLogisticraftFluid(Fluid fluid, int flammability, boolean flammable, Color color) {
		super(fluid, Material.water);

		setDensity(fluid.getDensity());

		this.flammability = flammability;
		this.flammable = flammable;

		this.color = color;
	}

	@Override
	public Fluid getFluid() {
		return FluidRegistry.getFluid(fluidName);
	}

	/**
	 * Copied from
	 * {@link BlockLiquid#randomDisplayTick(IBlockState, World, BlockPos, Random)}
	 * and modified to have colored particles.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World worldIn, int x, int y, int z, Random rand) {
		if (this.blockMaterial == Material.water) {
			int i = Math.round(8 * ((BlockFluidClassic) worldIn.getBlock(x, y, z)).getFilledPercentage(worldIn, x, y, z));

			if (i > 0 && i < 8) {
				if (getFluid().getViscosity(worldIn, x, y, z) < 5000 && rand.nextInt(64) == 0) {
					worldIn.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "minecraft:liquid.water", rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() + 0.5F, false);
				}
			} else if (rand.nextInt(10) == 0) {
				worldIn.spawnParticle("underwater", x + rand.nextFloat(), y + rand.nextFloat(), z + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
			}
		}

		if (this.blockMaterial == Material.lava && worldIn.getBlock(x, y + 1, z).getMaterial() == Material.air && !worldIn.getBlock(x, y + 1, z).isOpaqueCube()) {
			if (rand.nextInt(100) == 0) {
				double d8 = x + rand.nextFloat();
				double d4 = y + worldIn.getBlock(x, y, z).getBlockBoundsMaxY();
				double d6 = z + rand.nextFloat();
				worldIn.spawnParticle("lava", d8, d4, d6, 0.0D, 0.0D, 0.0D);
				worldIn.playSound(d8, d4, d6, "minecraft:liquid.lavapop", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
			}

			if (rand.nextInt(200) == 0) {
				worldIn.playSound(x, y, z, "minecraft:liquid.lava", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
			}
		}
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return !block.getMaterial().isLiquid() && super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return !block.getMaterial().isLiquid() && super.displaceIfPossible(world, x, y, z);
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return flammable ? 30 : 0;
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, @Nullable ForgeDirection face) {
		return flammability;
	}

	private static boolean isFlammable(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return block.isFlammable(world, x, y, z, ForgeDirection.UP);
	}

	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return flammable;
	}

	@Override
	public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
		return flammable && flammability == 0;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public Material getMaterial() {
		// Fahrenheit 451 = 505.928 Kelvin
		// The temperature at which book-paper catches fire, and burns.
		if (temperature > 505) {
			return Material.lava;
		} else {
			return super.getMaterial();
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		super.updateTick(world, x, y, z, rand);

		// Start fires if the fluid is lava-like
		if (getMaterial() == Material.lava) {
			int rangeUp = rand.nextInt(3);

			for (int i = 0; i < rangeUp; ++i) {
				x += rand.nextInt(3) - 1;
				++y;
				z += rand.nextInt(3) - 1;
				Block block = world.getBlock(x, y, z);
				if (block.getMaterial() == Material.air) {
					if (isNeighborFlammable(world, x, y, z)) {
						world.setBlock(x, y, z, Blocks.fire);
						return;
					}
				} else if (block.getMaterial().blocksMovement()) {
					return;
				}
			}

			if (rangeUp == 0) {
				int startX = x;
				int startZ = z;

				for (int i = 0; i < 3; ++i) {
					x = startX + rand.nextInt(3) - 1;
					z = startZ + rand.nextInt(3) - 1;

					if (world.isAirBlock(x, y + 1, z) && isFlammable(world, x, y, z)) {
						world.setBlock(x, y + 1, z, Blocks.fire);
					}
				}
			}
		}

		// explode if very flammable and near fire
		int flammability = getFlammability(world, x, y, z, null);
		if (flammability > 0) {
			// Explosion size is determined by flammability, up to size 4.
			float explosionSize = 4F * flammability / 300F;
			if (explosionSize > 1.0 && isNearFire(world, x, y, z)) {
				world.setBlock(x, y, z, Blocks.fire);
				world.newExplosion(null, x, y, z, explosionSize, true, true);
			}
		}
	}

	private static boolean isNeighborFlammable(World world, int x, int y, int z) {
		return isFlammable(world, x - 1, y, z) || isFlammable(world, x + 1, y, z) || isFlammable(world, x, y, z - 1) || isFlammable(world, x, y, z + 1) || isFlammable(world, x, y - 1, z) || isFlammable(world, x, y + 1, z);
	}

	private static boolean isNearFire(World world, int x, int y, int z) {
		AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
		return world.isAABBInMaterial(boundingBox, Material.fire);
	}
}