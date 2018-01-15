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

public class ContainerFractionator extends Container {
	
	public TileEntityFractionator tEntity;
	
	/** Last time left for fuel to be used up. (in ticks) */
	public int lastBurnTime;
	/** Last time it takes to burn the current item in slot 1. (in ticks) */
	public int lastItemBurnTime;
	/** Last time left to process the item in slot 0. (in ticks) */
	public int lastProcessTime;

	public ContainerFractionator(InventoryPlayer inventory, TileEntityFractionator tEntity) {
		this.tEntity = tEntity;
		
		this.addSlotToContainer(new Slot(tEntity, 0, 56, 25)); // Input
		this.addSlotToContainer(new Slot(tEntity, 1, 8, 54)); // Fuel
		
		this.addSlotToContainer(new Slot(tEntity, 6, 154, 54)); // Tank Slot
		
		this.addSlotToContainer(new SlotFurnace(inventory.player, tEntity, 2, 114, 25)); // Output 1
		this.addSlotToContainer(new SlotFurnace(inventory.player, tEntity, 3, 132, 25)); // Output 2
		this.addSlotToContainer(new SlotFurnace(inventory.player, tEntity, 4, 114, 43)); // Output 3
		this.addSlotToContainer(new SlotFurnace(inventory.player, tEntity, 5, 132, 43)); // Output 4
		
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(inventory, 9 + (i * 9) + j, 8 + (j * 18), 84 + (i * 18)));
			}
		}
		
		for(int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inventory, i, 8 + (i * 18), 142));
		}
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting iCrafting) {
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, this.tEntity.processTime);
		iCrafting.sendProgressBarUpdate(this, 1, this.tEntity.burnTime);
		iCrafting.sendProgressBarUpdate(this, 2, this.tEntity.currentItemBurnTime);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.crafters.size(); i++) {
			ICrafting iCrafting = (ICrafting) this.crafters.get(i);
			if(this.lastProcessTime != this.tEntity.processTime) {
				iCrafting.sendProgressBarUpdate(this, 0, this.tEntity.processTime);
			}
			if(this.lastProcessTime != this.tEntity.burnTime) {
				iCrafting.sendProgressBarUpdate(this, 1, this.tEntity.burnTime);
			}
			if(this.lastItemBurnTime != this.tEntity.currentItemBurnTime) {
				iCrafting.sendProgressBarUpdate(this, 2, this.tEntity.currentItemBurnTime);
			}
		}
		
		this.lastProcessTime = this.tEntity.processTime;
		this.lastBurnTime = this.tEntity.burnTime;
		this.lastItemBurnTime = this.tEntity.currentItemBurnTime;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value) {
		switch(id) {
		case 0:
			this.tEntity.processTime = value;
			break;
		case 1:
			this.tEntity.burnTime = value;
			break;
		case 2:
			this.tEntity.currentItemBurnTime = value;
			break;
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack iS = null;
		Slot slot = this.getSlot(index);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack tempStack = slot.getStack();
			iS = tempStack.copy();

			if(index < tEntity.getSizeInventory()) {
				if(!this.mergeItemStack(tempStack, tEntity.getSizeInventory(), tEntity.getSizeInventory()+36, true)) {
					return null;
				}
				slot.onSlotChange(tempStack, iS);
			} else {
				if (tEntity.getItemBurnTime(tempStack) > 0) {
					//TODO: Cannot Shift-click fuel In
					if(!this.mergeItemStack(tempStack, 0, 0, false)) {
						return null;
					}
				} else {
					if (!this.mergeItemStack(tempStack, 0, tEntity.getSizeInventory(), false)) {
						return null;
					}
				}
			}
			
			if(tempStack.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			
			if( tempStack.stackSize == iS.stackSize) {
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

}
