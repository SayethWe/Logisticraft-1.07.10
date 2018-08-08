package com.sinesection.logisticraft.net;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.Unpooled;

public abstract class LogisticraftPacket implements ILogisticraftPacket {
	@Override
	public final FMLProxyPacket getPacket() {
		LogisticraftPacketBuffer data = new LogisticraftPacketBuffer(Unpooled.buffer());

		IPacketId id = getPacketId();
		data.writeByte(id.ordinal());
		writeData(data);

		return new FMLProxyPacket(data, PacketHandler.channelId);
	}

	protected abstract void writeData(LogisticraftPacketBuffer data);
}