package com.sinesection.logisticraft.net.packet;

import com.sinesection.logisticraft.net.ILogisticraftPacketHandlerClient;
import com.sinesection.logisticraft.net.LogisticraftPacketBuffer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class PacketHandlerDummyClient extends PacketHandlerDummy implements ILogisticraftPacketHandlerClient {
	public static final PacketHandlerDummyClient INSTANCE = new PacketHandlerDummyClient();

	private PacketHandlerDummyClient() {

	}

	@Override
	public void onPacketData(LogisticraftPacketBuffer data, EntityPlayer player) {

	}
}