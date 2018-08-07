package com.sinesection.logisticraft.net;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public interface ILogisticraftPacket {
	FMLProxyPacket getPacket();

	IPacketId getPacketId();
}