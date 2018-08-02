package com.sinesection.logisticraft.network;

import com.sinesection.logisticraft.Constants;
import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.network.packet.handlers.LogisticraftClientPacketHandler;
import com.sinesection.logisticraft.network.packet.handlers.LogisticraftServerPacketHandler;

import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

public class LogisticraftNetwork {
	
	public static final String NETWORK_CHANNEL_NAME = Constants.MOD_ID;
	public static FMLEventChannel logisticraftNetworkChannel;
	
	public static void registerChannels() {
		logisticraftNetworkChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(NETWORK_CHANNEL_NAME);
	}
	
	public static void registerEventHandlers() {
		logisticraftNetworkChannel.register(new LogisticraftServerPacketHandler());
		logisticraftNetworkChannel.register(new LogisticraftClientPacketHandler());
	}

}
