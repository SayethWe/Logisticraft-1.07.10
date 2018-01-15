package com.sinesection.logisticraft.container;

import com.sinesection.logisticraft.block.tileentity.TileEntityFractionator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class ContainerFractionator extends Container implements IOnInventoryChangedListener {

	public TileEntityFractionator tEntity;

	/** Last time left for fuel to be used up. (in ticks) */
	public int lastBurnTime;
	/** Last time it takes to burn the current item in slot 1. (in ticks) */
	public int lastItemBurnTime;
	/** Last time left to process the item in slot 0. (in ticks) */
	public int lastProcessTime;
	/** Last tank */
	private FluidTank lastTank;
	private int tempFluidId;

	public ContainerFractionator(InventoryPlayer inventory, TileEntityFractionator tEntity) {
		this.tEntity = tEntity;

		this.addSlotToContainer(new Slot(tEntity, 0, 32, 25)); // Input
		this.addSlotToContainer(new Slot(tEntity, 1, 8, 54)); // Fuel

		this.addSlotToContainer(new SlotFurnace(inventory.player, tEntity, 2, 90, 25)); // Output
																						// 1
		this.addSlotToContainer(new SlotFurnace(inventory.player, tEntity, 3, 108, 25)); // Output
																							// 2
		this.addSlotToContainer(new SlotFurnace(inventory.player, tEntity, 4, 90, 43)); // Output
																						// 3
		this.addSlotToContainer(new SlotFurnace(inventory.player, tEntity, 5, 108, 43)); // Output
																							// 4

		this.addSlotToContainer(new BucketSlot(tEntity, 6, 152, 33)); // Tank
																		// Input
																		// Slot
		this.addSlotToContainer(new BucketResultSlot(tEntity, 7, 152, 54)); // Tank
																			// Output
																			// Slot

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(inventory, 9 + (i * 9) + j, 8 + (j * 18), 84 + (i * 18)));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inventory, i, 8 + (i * 18), 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting iCrafting) {
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, this.tEntity.processTime);
		iCrafting.sendProgressBarUpdate(this, 1, this.tEntity.burnTime);
		iCrafting.sendProgressBarUpdate(this, 2, this.tEntity.currentItemBurnTime);
		if (this.tEntity.getOutputTank().getFluid() != null) {
			iCrafting.sendProgressBarUpdate(this, 3, this.tEntity.getOutputTank().getFluid().getFluidID());
			iCrafting.sendProgressBarUpdate(this, 4, this.tEntity.getOutputTank().getFluidAmount());
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting iCrafting = (ICrafting) this.crafters.get(i);
			if (this.lastProcessTime != this.tEntity.processTime) {
				iCrafting.sendProgressBarUpdate(this, 0, this.tEntity.processTime);
			}
			if (this.lastProcessTime != this.tEntity.burnTime) {
				iCrafting.sendProgressBarUpdate(this, 1, this.tEntity.burnTime);
			}
			if (this.lastItemBurnTime != this.tEntity.currentItemBurnTime) {
				iCrafting.sendProgressBarUpdate(this, 2, this.tEntity.currentItemBurnTime);
			}
			if (lastTank != null && !lastTank.equals(this.tEntity.getOutputTank()) && this.tEntity.getOutputTank().getFluid() != null) {
				iCrafting.sendProgressBarUpdate(this, 3, this.tEntity.getOutputTank().getFluid().getFluidID());
				iCrafting.sendProgressBarUpdate(this, 4, this.tEntity.getOutputTank().getFluid().amount);
			}
		}

		this.lastProcessTime = this.tEntity.processTime;
		this.lastBurnTime = this.tEntity.burnTime;
		this.lastItemBurnTime = this.tEntity.currentItemBurnTime;
		this.lastTank = this.tEntity.getOutputTank();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value) {
		switch (id) {
		case 0:
			this.tEntity.processTime = value;
			break;
		case 1:
			this.tEntity.burnTime = value;
			break;
		case 2:
			this.tEntity.currentItemBurnTime = value;
			break;
		case 3:
			this.tempFluidId = value;
			break;
		case 4:
			FluidTank tank = this.tEntity.getOutputTank();
			tank.setFluid(new FluidStack(FluidRegistry.getFluid(tempFluidId), value));
			break;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack iS = null;
		Slot slot = this.getSlot(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack tempStack = slot.getStack();
			iS = tempStack.copy();

			if (index < tEntity.getSizeInventory()) {
				if (!this.mergeItemStack(tempStack, tEntity.getSizeInventory(), tEntity.getSizeInventory() + 36, true)) {
					return null;
				}
				slot.onSlotChange(tempStack, iS);
			} else if (tEntity.getItemBurnTime(tempStack) > 0) {
				// TODO: Cannot Shift-click fuel In
				if (!this.mergeItemStack(tempStack, 0, 0, false)) {
					return null;
				}
			} else if (!FluidContainerRegistry.isEmptyContainer(iS) || !mergeItemStack(iS, 36, 37, false)) {
				return null;
			} else {
				if (!this.mergeItemStack(tempStack, 0, tEntity.getSizeInventory(), false)) {
					return null;
				}
			}

			if (tempStack.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (tempStack.stackSize == iS.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, tempStack);

		}

		return iS;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tEntity.isUseableByPlayer(player);
	}

	@Override
	public void onInventoryChanged() {
		if (this.tEntity.getStackInSlot(5) == null)
			return;
		ItemStack inputSlot = ItemStack.copyItemStack(this.tEntity.getStackInSlot(5));
		inputSlot.stackSize = 1;
		ItemStack outputSlot = ItemStack.copyItemStack(this.tEntity.getStackInSlot(6));
		FluidTank tank = this.tEntity.getOutputTank();
		boolean success = false;
		if (FluidContainerRegistry.isEmptyContainer(inputSlot)) {
			if (tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false) != null && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME) {
				if (!addStackToOutput(FluidContainerRegistry.fillFluidContainer(tank.getFluid(), inputSlot), false))
					return;
				outputSlot = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), inputSlot);
				tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
				success = true;
			}

		}
		if (success) {
			this.tEntity.getStackInSlot(5).stackSize--;
			if (this.tEntity.getStackInSlot(5).stackSize == 0)
				this.tEntity.setInventorySlotContents(5, null);
			this.tEntity.setOutputTank(tank);
			addStackToOutput(outputSlot, true);
		}
	}

	private boolean addStackToOutput(ItemStack stack, boolean doPut) {
		ItemStack output = tEntity.getStackInSlot(6);
		if (stack == null) {
			if (doPut)
				tEntity.markDirty();
			return true;
		}
		if (output == null) {
			if (doPut) {
				tEntity.setInventorySlotContents(6, stack);
			}
			return true;
		} else if (stack.isItemEqual(output) && (output.stackSize + stack.stackSize) <= output.getMaxStackSize()) {
			if (doPut) {
				tEntity.incrStackSize(1, stack.stackSize > 0 ? stack.stackSize : 1);
			}
			return true;
		} else {
			return false;
		}
	}

}
