package com.sinesection.logisticraft.api;

import net.minecraft.nbt.NBTTagCompound;

public interface INbtReadable {
	void readFromNBT(NBTTagCompound nbt);
}