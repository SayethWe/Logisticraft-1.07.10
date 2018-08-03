package com.sinesection.logisticraft.net;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public interface ILogisticraftPacketHandlerClient extends ILogisticraftPacketHandler {
	void onPacketData(PacketBufferLogisticraft data, EntityPlayer player) throws IOException;
}