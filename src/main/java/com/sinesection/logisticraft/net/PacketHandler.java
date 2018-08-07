package com.sinesection.logisticraft.net;

import java.io.IOException;

import com.google.common.base.Preconditions;
import com.sinesection.utils.Log;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public class PacketHandler {
	public static final String channelId = "LogC";
	private final FMLEventChannel channel;

	public PacketHandler() {
		channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelId);
		channel.register(this);
	}

	@SubscribeEvent
	public void onPacket(ServerCustomPacketEvent event) {
		LogisticraftPacketBuffer data = new LogisticraftPacketBuffer(event.packet.payload());
		EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;

		byte packetIdOrdinal = data.readByte();
		PacketIdServer packetId = PacketIdServer.VALUES[packetIdOrdinal];
		ILogisticraftPacketHandlerServer packetHandler = packetId.getPacketHandler();
		data.retain();
		try {
			packetHandler.onPacketData(data, player);
			data.release();
		} catch (IOException e) {
			Log.error("Network Error", e);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPacket(ClientCustomPacketEvent event) {
		LogisticraftPacketBuffer data = new LogisticraftPacketBuffer(event.packet.payload());

		byte packetIdOrdinal = data.readByte();
		PacketIdClient packetId = PacketIdClient.VALUES[packetIdOrdinal];
		ILogisticraftPacketHandlerClient packetHandler = packetId.getPacketHandler();
		data.retain();
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		Preconditions.checkNotNull(player, "Tried to send data to client before the player exists.");
		try {
			packetHandler.onPacketData(data, player);
		} catch (IOException e) {
			Log.error("Network Error", e);
		}
	}

	public void sendPacket(FMLProxyPacket packet, EntityPlayerMP player) {
		channel.sendTo(packet, player);
	}
}