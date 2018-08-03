package com.sinesection.logisticraft.net;

import java.io.IOException;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;

public abstract class LogisticraftPacket implements ILogisticraftPacket {
	@Override
	public final FMLProxyPacket getPacket() {
		PacketBufferLogisticraft data = new PacketBufferLogisticraft(Unpooled.buffer());

		IPacketId id = getPacketId();
		data.writeByte(id.ordinal());
		writeData(data);

		return new FMLProxyPacket(data, PacketHandler.channelId);
	}

	protected abstract void writeData(PacketBufferLogisticraft data);
}