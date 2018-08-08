package com.sinesection.logisticraft.network.packet.handlers;

import java.io.IOException;

import com.sinesection.logisticraft.network.LogisticraftNetwork;
import com.sinesection.logisticraft.network.packet.LogisticraftPacket;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;

public class LogisticraftClientPacketHandler extends LogisticraftServerPacketHandler {
	@SubscribeEvent
	public void onClientPacket(ClientCustomPacketEvent event) throws IOException {
		channelName = event.packet.channel();

		if (channelName == LogisticraftNetwork.NETWORK_CHANNEL_NAME) {
			LogisticraftPacket.processPacketOnClientSide(event.packet.payload(), event.packet.getTarget());
		}
	}
}