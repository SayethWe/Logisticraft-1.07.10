package com.sinesection.logisticraft.net;

import com.sinesection.logisticraft.net.packet.PacketHandlerDummy;

import net.minecraft.entity.player.EntityPlayerMP;

public class PacketHandlerDummyServer extends PacketHandlerDummy implements ILogisticraftPacketHandlerServer {
	public static final PacketHandlerDummyServer instance = new PacketHandlerDummyServer();

	private PacketHandlerDummyServer() {

	}

	@Override
	public void onPacketData(PacketBufferLogisticraft data, EntityPlayerMP player) {

	}
}