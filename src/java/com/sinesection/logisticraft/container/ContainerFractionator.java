package com.sinesection.logisticraft.container;

import com.sinesection.logisticraft.block.tileentity.TileEntityFractionator;
import com.sinesection.logisticraft.crafting.DryDistillerRecipeManager;
import com.sinesection.logisticraft.crafting.FractionatorRecipeManager;
import com.sinesection.logisticraft.fluid.LogisticraftFluidTank;

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

public class ContainerFractionator extends Container {

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
				iCrafting.sendProgressBarUpdate(this, 4, this.tEntity.getOutputTank().getFluidAmount());
			}
		}

		this.lastProcessTime = this.tEntity.processTime;
		this.lastBurnTime = this.tEntity.burnTime;
		this.lastItemBurnTime = this.tEntity.currentItemBurnTime;
		this.lastTank = this.tEntity.getOutputTank().copy();
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
			LogisticraftFluidTank tank = this.tEntity.getOutputTank();
			tank.setFluid(new FluidStack(FluidRegistry.getFluid(tempFluidId), value));
			this.tEntity.setOutputTank(tank);
			break;
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
			if (index == TileEntityFractionator.SLOT_FUEL) {
				if (this.mergeItemStack(tempItemStack, playerInvStart, playerInvEnd, true))
					return null;
				slot.onSlotChange(tempItemStack, itemStack);
			} else if (index != TileEntityFractionator.SLOT_FUEL && index != TileEntityFractionator.SLOT_INPUT && index != TileEntityFractionator.SLOT_TANK_INPUT && index != TileEntityFractionator.SLOT_TANK_OUTPUT) {
				if (FractionatorRecipeManager.findMatchingRecipe(tempItemStack) != null || DryDistillerRecipeManager.findMatchingRecipe(tempItemStack) != null) {
					if (!this.mergeItemStack(tempItemStack, TileEntityFractionator.SLOT_INPUT, TileEntityFractionator.SLOT_INPUT + 1, false)) {
						return null;
					}
				} else if (this.tEntity.isItemFuel(tempItemStack)) {
					if (!this.mergeItemStack(tempItemStack, TileEntityFractionator.SLOT_FUEL, TileEntityFractionator.SLOT_FUEL + 1, false)) {
						return null;
					}
				} else if (FluidContainerRegistry.isEmptyContainer(tempItemStack)) {
					if (!this.mergeItemStack(tempItemStack, TileEntityFractionator.SLOT_TANK_INPUT, TileEntityFractionator.SLOT_TANK_INPUT + 1, false)) {
						return null;
					}
				} else if (index >= playerInvStart && index < playerHotbarStart) {
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
