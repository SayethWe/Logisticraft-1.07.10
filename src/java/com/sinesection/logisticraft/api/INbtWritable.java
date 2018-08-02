package com.sinesection.logisticraft.api;

import net.minecraft.nbt.NBTTagCompound;

public interface INbtWritable {
	NBTTagCompound writeToNBT(NBTTagCompound nbt);
}