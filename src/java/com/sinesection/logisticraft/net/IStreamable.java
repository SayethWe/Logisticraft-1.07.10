package com.sinesection.logisticraft.net;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public interface IStreamable {
	void writeData(PacketBufferLogisticraft data);

	@SideOnly(Side.CLIENT)
	void readData(PacketBufferLogisticraft data) throws IOException;
}