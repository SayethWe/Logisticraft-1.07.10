package com.sinesection.logisticraft.net;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;

public interface ILogisticraftPacketHandlerServer extends ILogisticraftPacketHandler {
	void onPacketData(LogisticraftPacketBuffer data, EntityPlayerMP player) throws IOException;
}