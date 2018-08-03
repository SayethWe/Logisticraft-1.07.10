package com.sinesection.utils;

import com.google.common.base.Preconditions;
import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.net.ILogisticraftPacketClient;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class NetworkUtil {
	public static <P extends ILogisticraftPacketClient> void sendNetworkPacket(P packet, int x, int y, int z, World world) {
		if (!(world instanceof WorldServer)) {
			return;
		}

		WorldServer worldServer = (WorldServer) world;
		PlayerChunkMap playerManager = worldServer.chun.getPlayerChunkMap();

		int chunkX = x >> 4;
		int chunkZ = z >> 4;

		for (Object playerObj : world.playerEntities) {
			if (playerObj instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) playerObj;

				if (playerManager.isPlayerWatchingChunk(player, chunkX, chunkZ)) {
					sendToPlayer(packet, player);
				}
			}
		}
	}

	public static void sendToPlayer(ILogisticraftPacketClient packet, EntityPlayer entityplayer) {
		if (!(entityplayer instanceof EntityPlayerMP) || entityplayer instanceof FakePlayer) {
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) entityplayer;
		Main.getPacketHandler().sendPacket(packet.getPacket(), player);
	}

	public static void inventoryChangeNotify(EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void sendToServer(ILogsticraftPacketServer packet) {
		NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
		Preconditions.checkNotNull(netHandler, "Tried to send packet before netHandler (client world) exists.");
		netHandler.sendPacket(packet.getPacket());
	}
}