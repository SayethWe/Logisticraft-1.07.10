package com.sinesection.logisticraft.network;

import com.sinesection.logisticraft.block.tileentity.TileEntityMixer;
import com.sinesection.logisticraft.fluid.LogisticraftFluidTank;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ContainerMixer extends Container {

	public TileEntityMixer tEntity;

	/** Last time left for fuel to be used up. (in ticks) */
	public int lastTempature;
	/** Last time left to process the item in slot 0. (in ticks) */
	public int lastProcessTime;
	/** Last fluid ids in the tanks */
	private int[] lastFluidIds;
	
	public ContainerMixer(InventoryPlayer inventory, TileEntityMixer tEntity) {
		this.tEntity = tEntity;
		lastFluidIds = new int[this.tEntity.getNumTanks()];

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
		for(int i = 0; i < this.tEntity.getNumTanks(); i++) {
			if (this.tEntity.getTank(i).getFluid() != null) {
				int craftersIndex = 1 + i * 2;
				iCrafting.sendProgressBarUpdate(this, craftersIndex, this.tEntity.getTank(i).getFluid().getFluidID());
				iCrafting.sendProgressBarUpdate(this, craftersIndex + 1, this.tEntity.getTank(i).getFluidAmount());
			}
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
			for (int tankIndex = 0; tankIndex < this.tEntity.getNumTanks(); tankIndex++) {
				int craftersIndex = 1 + tankIndex * 2;
				if (this.lastFluidIds[tankIndex] != this.tEntity.getTank(tankIndex).getFluid().getFluidID()) {
					iCrafting.sendProgressBarUpdate(this, craftersIndex, this.tEntity.getTank(tankIndex).getFluid().getFluidID());
					iCrafting.sendProgressBarUpdate(this, craftersIndex + 1, this.tEntity.getTank(tankIndex).getFluidAmount());
				}
			}
		}

		this.lastProcessTime = this.tEntity.processTime;
		for (int tankIndex = 0; tankIndex < this.tEntity.getNumTanks(); tankIndex++) {
			this.lastFluidIds[tankIndex] = this.tEntity.getTank(tankIndex).getFluid().getFluidID();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value) {
		switch (id) {
		case 0:
			this.tEntity.processTime = value;
			break;
		default:
			if (id > 0) {
				if ((id - 1) % 2 == 0) {
					int tankIndex = (id - 1) / 2;
					System.out.println(id + ", " + tankIndex);
					lastFluidIds[tankIndex] = value;
					break;
				} else if((id - 1) % 2 == 1) {
					int tankIndex = (id - 1) / 2;
					LogisticraftFluidTank tank = new LogisticraftFluidTank(this.tEntity.getTank(tankIndex).getCapacity());
					tank.setFluid(new FluidStack(FluidRegistry.getFluid(lastFluidIds[tankIndex]), value));
					this.tEntity.setTank((id - 1) / 2, tank);
					break;
				}
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemStack = null;
		Slot slot = this.getSlot(index);

		int playerInvStart = tEntity.getSizeInventory();
		int playerInvEnd = tEntity.getSizeInventory() + player.inventory.mainInventory.length;
		int playerHotbarStart = playerInvEnd - 9;

		if (slot != null && slot.getHasStack()) {
			ItemStack tempItemStack = slot.getStack();
			itemStack = tempItemStack.copy();
			if (index != TileEntityMixer.SLOT_INPUT) {
				if (index >= playerInvStart && index < playerHotbarStart) {
					if (!this.mergeItemStack(tempItemStack, playerHotbarStart, playerInvEnd, false)) {
						return null;
					}
				} else if (index >= playerHotbarStart && index < playerInvEnd) {
					if (!this.mergeItemStack(tempItemStack, playerInvStart, playerInvEnd, false)) {
						return null;
					}
				}
			} else if (!this.mergeItemStack(tempItemStack, playerInvStart, playerInvEnd, false)) {
				return null;
			}

			if (tempItemStack.stackSize == 0)
				slot.putStack((ItemStack) null);
			else
				slot.onSlotChanged();

			if (tempItemStack.stackSize == itemStack.stackSize)
				return null;

			slot.onPickupFromSlot(player, itemStack);
		}

		return itemStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tEntity.isUseableByPlayer(player);
	}

}
