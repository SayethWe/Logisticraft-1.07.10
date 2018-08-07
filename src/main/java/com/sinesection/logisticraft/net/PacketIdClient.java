package com.sinesection.logisticraft.net;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.net.packet.PacketHandlerDummyClient;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum PacketIdClient implements IPacketId {
	INVALID,

	// Core Gui
	ERROR_UPDATE,
	ERROR_UPDATE_ENTITY,
	GUI_UPDATE,
	GUI_UPDATE_ENTITY,
	GUI_LAYOUT_SELECT,
	GUI_ENERGY,

	// Core Tile Entities
	TILE_FORESTRY_UPDATE,
	ITEMSTACK_DISPLAY,
	FX_SIGNAL,
	TANK_LEVEL_UPDATE,

	// Factory
	WORKTABLE_MEMORY_UPDATE,
	WORKTABLE_CRAFTING_UPDATE,

	// Sorting
	GUI_UPDATE_FILTER;

	public static final PacketIdClient[] VALUES = values();

	@SideOnly(Side.CLIENT)
	@Nullable
	private ILogisticraftPacketHandlerClient packetHandler;

	@SideOnly(Side.CLIENT)
	public void setPacketHandler(ILogisticraftPacketHandlerClient packetHandler) {
		this.packetHandler = packetHandler;
	}

	@SideOnly(Side.CLIENT)
	public ILogisticraftPacketHandlerClient getPacketHandler() {
		if (packetHandler == null) {
			return PacketHandlerDummyClient.INSTANCE;
		}
		return packetHandler;
	}
}