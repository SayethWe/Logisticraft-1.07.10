package com.sinesection.logisticraft.net;

public enum PacketIdServer implements IPacketId {
	INVALID,

	// Core Gui
	GUI_SELECTION_REQUEST;

	// TODO Other server packet types.

	public static final PacketIdServer[] VALUES = values();

	private ILogisticraftPacketHandlerServer packetHandler;

	PacketIdServer() {
		this.packetHandler = PacketHandlerDummyServer.instance;
	}

	public void setPacketHandler(ILogisticraftPacketHandlerServer packetHandler) {
		this.packetHandler = packetHandler;
	}

	public ILogisticraftPacketHandlerServer getPacketHandler() {
		return packetHandler;
	}
}