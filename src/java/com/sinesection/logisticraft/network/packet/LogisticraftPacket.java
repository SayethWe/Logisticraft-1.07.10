package com.sinesection.logisticraft.network.packet;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

public class LogisticraftPacket {

	public static final int PACKET_TYPE_TILE_ENTITY_DRY_DISTILLER = 0;
	public static final int PACKET_TYPE_TILE_ENTITY_FRACTIONATOR = 1;

	public LogisticraftPacket() {
	}

	public static void processPacketOnClientSide(ByteBuf buf, Side side) throws IOException {
		if (side == Side.CLIENT) {
			ByteBufInputStream bbis = new ByteBufInputStream(buf);

			int packetTypeID = bbis.readInt();

			switch (packetTypeID) {
			
			}

			bbis.close();
		}
	}

	public static void processPacketOnServerSide(ByteBuf buf, Side side) throws IOException {
		if (side == Side.SERVER) {
			ByteBufInputStream bbis = new ByteBufInputStream(buf);

			int packetTypeID = bbis.readInt();

			switch (packetTypeID) {
			
			}

			bbis.close();
		}
	}
}
