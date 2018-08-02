package com.sinesection.logisticraft.fluid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sinesection.logisticraft.api.INbtReadable;
import com.sinesection.logisticraft.api.INbtWritable;
import com.sinesection.logisticraft.net.IStreamable;
import com.sinesection.logisticraft.net.PacketBufferLogisticraft;
import com.sinesection.logisticraft.render.EnumTankLevel;
import com.sinesection.logisticraft.tiles.ILiquidTankTile;
import com.sinesection.utils.NetworkUtil;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import net.minecraftforge.fluids.TileFluidHandler;

public class TankManager implements ITankManager, ITankUpdateHandler, IStreamable, INbtWritable, INbtReadable {

	private final List<StandardTank> tanks = new ArrayList<>();

	// for container updates, keeps track of the fluids known to each client (container)
	private final Table<Container, Integer, FluidStack> prevFluidStacks = HashBasedTable.create();

	// tank tile updates, for blocks that show fluid levels on the outside
	@Nullable
	private final ILiquidTankTile tile;
	private final List<EnumTankLevel> tankLevels = new ArrayList<>();

	public TankManager() {
		this.tile = null;
	}

	public TankManager(ILiquidTankTile tile, StandardTank... tanks) {
		this.tile = tile;
		addAll(Arrays.asList(tanks));
	}

	public final boolean addAll(Collection<? extends StandardTank> collection) {
		boolean addedAll = true;
		for (StandardTank tank : collection) {
			addedAll &= add(tank);
		}
		return addedAll;
	}

	public boolean add(StandardTank tank) {
		boolean added = tanks.add(tank);
		int index = tanks.indexOf(tank);
		tank.setTankUpdateHandler(this);
		tank.setTankIndex(index);
		tankLevels.add(EnumTankLevel.rateTankLevel(tank));
		return added;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		NBTTagList tagList = new NBTTagList();
		for (byte slot = 0; slot < tanks.size(); slot++) {
			StandardTank tank = tanks.get(slot);
			if (tank.getFluid() != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("tank", slot);
				tank.writeToNBT(tag);
				tagList.appendTag(tag);
			}
		}
		data.setTag("tanks", tagList);
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		NBTList<NBTTagCompound> tagList = NBTUtilLogisticraft.getNBTList(data, "tanks", NBTUtilLogisticraft.EnumNBTType.COMPOUND);
		for (NBTTagCompound tag : tagList) {
			int slot = tag.getByte("tank");
			if (slot >= 0 && slot < tanks.size()) {
				StandardTank tank = tanks.get(slot);
				tank.readFromNBT(tag);
				updateTankLevels(tank, false);
			}
		}
	}

	@Override
	public void writeData(PacketBufferLogisticraft data) {
		for (StandardTank tank : tanks) {
			tank.writeData(data);
		}
	}

	@Override
	public void readData(PacketBufferLogisticraft data) throws IOException {
		for (StandardTank tank : tanks) {
			tank.readData(data);
		}
	}

	@Override
	public void containerAdded(Container container, ICrafting player) {
		if (!(player instanceof EntityPlayerMP)) {
			return;
		}

		List<ICrafting> crafters = Collections.singletonList(player);

		for (StandardTank tank : tanks) {
			sendTankUpdate(container, crafters, tank);
		}
	}

	@Override
	public void containerRemoved(Container container) {
		for (StandardTank tank : tanks) {
			prevFluidStacks.remove(container, tank.getTankIndex());
		}
	}

	@Override
	public void sendTankUpdate(Container container, List<ICrafting> crafters) {
		for (StandardTank tank : tanks) {
			sendTankUpdate(container, crafters, tank.getTankIndex());
		}
	}

	private void sendTankUpdate(Container container, List<ICrafting> crafters, int tankIndex) {
		StandardTank tank = tanks.get(tankIndex);
		if (tank == null) {
			return;
		}

		FluidStack fluidStack = tank.getFluid();
		FluidStack prev = prevFluidStacks.get(container, tankIndex);
		if (FluidHelper.areFluidStacksEqual(fluidStack, prev)) {
			return;
		}

		sendTankUpdate(container, crafters, tank);
	}

	private void sendTankUpdate(Container container, Iterable<ICrafting> crafters, StandardTank tank) {
		if (tile != null) {
			int tankIndex = tank.getTankIndex();
			FluidStack fluid = tank.getFluid();
			ILogisticraftPacketClient packet = new PacketTankLevelUpdate(tile, tankIndex, fluid);
			for (ICrafting crafter : crafters) {
				if (crafter instanceof EntityPlayerMP) {
					NetworkUtil.sendToPlayer(packet, (EntityPlayerMP) crafter);
				}
			}

			if (fluid == null) {
				prevFluidStacks.remove(container, tankIndex);
			} else {
				prevFluidStacks.put(container, tankIndex, fluid.copy());
			}
		}
	}

	@Override
	public void processTankUpdate(int tankIndex, @Nullable FluidStack contents) {
		if (tankIndex < 0 || tankIndex > tanks.size()) {
			return;
		}
		StandardTank tank = tanks.get(tankIndex);
		tank.setFluid(contents);
	}

	@Override
	public IFluidTank getTank(int tankIndex) {
		return tanks.get(tankIndex);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		for (StandardTank tank : tanks) {
			if (tankAcceptsFluid(tank, resource)) {
				return fill(tank.getTankIndex(), resource, doFill);
			}
		}
		
		return EmptyFluidHandler.INSTANCE.fill(resource, doFill);
	}

	public int fill(int tankIndex, FluidStack resource, boolean doFill) {
		if (tankIndex < 0 || tankIndex >= tanks.size()) {
			return 0;
		}

		StandardTank tank = tanks.get(tankIndex);
		if (!tank.canFill()) {
			return 0;
		}

		return tank.fill(resource, doFill);
	}

	@Override
	public void updateTankLevels(StandardTank tank) {
		updateTankLevels(tank, true);
	}

	private void updateTankLevels(StandardTank tank, boolean sendUpdate) {
		if (!(tile instanceof IRenderHandler)) {
			return;
		}

		int tankIndex = tank.getTankIndex();
		EnumTankLevel tankLevel = EnumTankLevel.rateTankLevel(tank);
		if (tankLevel != tankLevels.get(tankIndex)) {
			tankLevels.set(tankIndex, tankLevel);
			if (sendUpdate) {
				PacketTankLevelUpdate tankLevelUpdate = new PacketTankLevelUpdate(tile, tankIndex, tank.getFluid());
				NetworkUtil.sendNetworkPacket(tankLevelUpdate, tile.getCoordinates(), tile.getWorldObj());
			}
		}
	}

	@Nullable
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		for (StandardTank tank : tanks) {
			if (tankCanDrain(tank)) {
				return drain(tank.getTankIndex(), maxDrain, doDrain);
			}
		}
		return EmptyFluidHandler.INSTANCE.drain(maxDrain, doDrain);
	}

	@Nullable
	public FluidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		if (tankIndex < 0 || tankIndex >= tanks.size()) {
			return null;
		}

		StandardTank tank = tanks.get(tankIndex);
		if (!tank.canDrain()) {
			return null;
		}

		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		for (StandardTank tank : tanks) {
			if (tankCanDrainFluid(tank, resource)) {
				return drain(tank.getTankIndex(), resource.amount, doDrain);
			}
		}
		return null;
	}

	@Override
	public FluidTankInfo[] getInfo() {
		IFluidTankProperties[] properties = new IFluidTankProperties[tanks.size()];
		for (int i = 0; i < tanks.size(); i++) {
			properties[i] = new FluidTankPropertiesWrapper(tanks.get(i));
		}
		return properties;
	}

	public FluidTankInfo getTankInfo(int tankIndex) {
		return tanks.get(tankIndex).getInfo();
	}

	@Nullable
	public FluidStack getFluid(int tankIndex) {
		return tanks.get(tankIndex).getFluid();
	}

	public int getFluidAmount(int tankIndex) {
		return tanks.get(tankIndex).getFluidAmount();
	}

	@Override
	public boolean canFillFluidType(FluidStack fluidStack) {
		for (StandardTank tank : tanks) {
			if (tank.canFillFluidType(fluidStack)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluidStack) {
		for (StandardTank tank : tanks) {
			if (tank.canDrainFluidType(fluidStack)) {
				return true;
			}
		}

		return false;
	}

	private static boolean tankAcceptsFluid(StandardTank tank, FluidStack fluidStack) {
		return tank.canFill() &&
				tank.fill(fluidStack, false) > 0;
	}

	private static boolean tankCanDrain(StandardTank tank) {
		if (!tank.canDrain()) {
			return false;
		}
		FluidStack drained = tank.drain(1, false);
		return drained != null && drained.amount > 0;
	}

	private static boolean tankCanDrainFluid(StandardTank tank, FluidStack fluidStack) {
		return Fluids.areEqual(tank.getFluidType(), fluidStack) &&
				tankCanDrain(tank);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void containerAdded(Container container, ICrafting crafter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTankUpdate(Container container, List<ICrafting> crafters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void containerRemoved(Container container) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IFluidTank getTank(int tankIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluidStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluidStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void processTankUpdate(int tankIndex, FluidStack contents) {
		// TODO Auto-generated method stub
		
	}
}