package com.sinesection.logisticraft.render;

import net.minecraftforge.fluids.IFluidTank;

public enum EnumTankLevel {

	EMPTY(0),
	NEAREMPTY(10),
	VERYLOW(20),
	ONEQUARTER(25),
	LOW(30),
	LESSHALF(40),
	HALF(50),
	MOREHALF(60),
	FULL(70),
	THREEQUARTERS(75),
	VERYFULL(80),
	NEARMAX(90),
	MAX(100);

	private final int level;

	EnumTankLevel(int level) {
		this.level = level;
	}

	public int getLevelScaled(int scale) {
		return level * scale / 100;
	}

	public static EnumTankLevel rateTankLevel(IFluidTank tank) {
		return rateTankLevel(100 * tank.getFluidAmount() / tank.getCapacity());
	}

	public static EnumTankLevel rateTankLevel(int scaled) {
		if (scaled < EMPTY.level)
			scaled = EMPTY.level;
		if (scaled > MAX.level)
			scaled = MAX.level;
		for (EnumTankLevel l : values()) {
			if (scaled < l.level + 9)
				return l;
		}
		return EMPTY;
	}
}