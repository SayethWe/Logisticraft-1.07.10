package com.sinesection.logisticraft.fluid;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.gui.tooltips.ToolTip;
import com.sinesection.logisticraft.net.IStreamable;
import com.sinesection.logisticraft.net.PacketBufferLogisticraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IStream;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class StandardTank extends FluidTank implements IStreamable {
	private static final int DEFAULT_COLOR = 0xFFFFFF;

	private ITankUpdateHandler tankUpdateHandler = FakeTankUpdateHandler.instance;
	private int tankIndex;

	@SideOnly(Side.CLIENT)
	@Nullable
	protected ToolTip toolTip;

	public StandardTank(int capacity, boolean canFill, boolean canDrain) {
		super(capacity);
	}

	public StandardTank(int capacity) {
		super(capacity);
	}

	public void setTankIndex(int index) {
		this.tankIndex = index;
	}

	public void setTankUpdateHandler(TankManager tankUpdateHandler) {
		this.tankUpdateHandler = tankUpdateHandler;
	}

	public int getTankIndex() {
		return tankIndex;
	}

	public int getColor() {
		Fluid f = getFluidType();
		if (f == null) {
			return DEFAULT_COLOR;
		}
		return f.getColor(getFluid());
	}

	public boolean isEmpty() {
		return getFluid() == null || getFluid().amount <= 0;
	}

	public boolean isFull() {
		return getFluid() != null && getFluid().amount == getCapacity();
	}

	public int getRemainingSpace() {
		return capacity - getFluidAmount();
	}

	@Nullable
	public Fluid getFluidType() {
		return getFluid() != null ? getFluid().getFluid() : null;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		int filled = super.fill(resource, doFill);
		if (doFill && filled > 0) {
			tankUpdateHandler.updateTankLevels(this);
		}
		return filled;
	}

	@Override
	@Nullable
	public FluidStack drain(int maxDrain, boolean doDrain) {
		FluidStack drained = super.drain(maxDrain, doDrain);
		if (doDrain && drained != null && drained.amount > 0) {
			tankUpdateHandler.updateTankLevels(this);
		}
		return drained;
	}

	@Override
	public String toString() {
		return String.format("Tank: %s, %d/%d", fluid != null && fluid.getFluid() != null ? fluid.getFluid().getName() : "Empty", getFluidAmount(), getCapacity());
	}

	protected boolean hasFluid() {
		FluidStack fluid = getFluid();
		return fluid != null && fluid.amount > 0 && fluid.getFluid() != null;
	}

	@Override
	public void writeData(PacketBufferLogisticraft data) {
		data.writeFluidStack(fluid);
	}

	@Override
	public void readData(PacketBufferLogisticraft data) {
		fluid = data.readFluidStack();
	}

	@SideOnly(Side.CLIENT)
	public ToolTip getToolTip() {
		if (toolTip == null) {
			toolTip = new TankToolTip(this);
		}
		return toolTip;
	}

	@SideOnly(Side.CLIENT)
	protected void refreshTooltip() {
		ToolTip toolTip = getToolTip();
		toolTip.clear();
		int amount = 0;
		FluidStack fluidStack = getFluid();
		if (fluidStack != null) {
			Fluid fluidType = fluidStack.getFluid();
			EnumRarity rarity = fluidType.getRarity();
			if (rarity == null) {
				rarity = EnumRarity.common;
			}
			toolTip.add(fluidType.getLocalizedName(getFluid()), rarity.rarityColor);
			amount = getFluid().amount;
		}
		String liquidAmount = I18n.format("for.gui.tooltip.liquid.amount", amount, getCapacity());
		toolTip.add(liquidAmount);
	}

	@SideOnly(Side.CLIENT)
	private static class TankToolTip extends ToolTip {
		private final StandardTank standardTank;

		public TankToolTip(StandardTank standardTank) {
			this.standardTank = standardTank;
		}

		@Override
		public void refresh() {
			standardTank.refreshTooltip();
		}
	}
}