package com.sinesection.utils;

import com.sinesection.logisticraft.net.ILogisticraftPacket;
import com.sinesection.logisticraft.net.PacketIdServer;

public interface ILogsticraftPacketServer extends ILogisticraftPacket {
	@Override
	PacketIdServer getPacketId();
}