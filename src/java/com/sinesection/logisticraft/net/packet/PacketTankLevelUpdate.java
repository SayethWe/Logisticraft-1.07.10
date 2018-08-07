package com.sinesection.logisticraft.net.packet;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.fluid.ITankManager;
import com.sinesection.logisticraft.net.ILogisticraftPacketClient;
import com.sinesection.logisticraft.net.ILogisticraftPacketHandlerClient;
import com.sinesection.logisticraft.net.LogisticraftPacket;
import com.sinesection.logisticraft.net.LogisticraftPacketBuffer;
import com.sinesection.logisticraft.net.PacketIdClient;
import com.sinesection.logisticraft.tiles.ILiquidTankTile;
import com.sinesection.utils.TileUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;

public class PacketTankLevelUpdate extends LogisticraftPacket implements ILogisticraftPacketClient {
	private final int Xpos, Ypos, Zpos;
	private final int tankIndex;
	@Nullable
	private final FluidStack contents;

	public PacketTankLevelUpdate(ILiquidTankTile tileEntity, int tankIndex, @Nullable FluidStack contents) {
		this.Xpos = tileEntity.getXCoord();
		this.Ypos = tileEntity.getXCoord();
		this.Zpos = tileEntity.getXCoord();
		this.tankIndex = tankIndex;
		this.contents = contents;
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.TANK_LEVEL_UPDATE;
	}

	@Override
	protected void writeData(LogisticraftPacketBuffer data) {
		data.writeInt(Xpos);
		data.writeInt(Ypos);
		data.writeInt(Zpos);
		data.writeVarIntToBuffer(tankIndex);
		data.writeFluidStack(contents);
	}

	@SideOnly(Side.CLIENT)
	public static class Handler implements ILogisticraftPacketHandlerClient {

		@Override
		public void onPacketData(LogisticraftPacketBuffer data, EntityPlayer player) {
			int Xpos = data.readVarIntFromBuffer();
			int Ypos = data.readVarIntFromBuffer();
			int Zpos = data.readVarIntFromBuffer();
			int tankIndex = data.readVarIntFromBuffer();
			FluidStack contents = data.readFluidStack();

			TileUtil.actOnTile(player.worldObj, Xpos, Ypos, Zpos, ILiquidTankTile.class, tile -> {
				ITankManager tankManager = tile.getTankManager();
				tankManager.processTankUpdate(tankIndex, contents);
			});
		}
	}
}
