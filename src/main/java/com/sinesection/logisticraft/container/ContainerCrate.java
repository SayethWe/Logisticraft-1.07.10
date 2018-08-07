package com.sinesection.logisticraft.container;

import com.sinesection.logisticraft.block.tileentity.TileEntityCrate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCrate extends Container{
	
	private TileEntityCrate tEntity;
	
	public ContainerCrate(InventoryPlayer inventory, TileEntityCrate tEntity) {
		this.tEntity = tEntity;
		
		this.addSlotToContainer(new Slot(tEntity,0,80,35)); //storage
		
		for(int i = 0; i < 3; i++) { //the inventory
			for(int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(inventory, 9 + (i * 9) + j, 8 + (j * 18), 84 + (i * 18)));
			}
		}
		
		for(int i = 0; i < 9; i++) { //the hotbar
			this.addSlotToContainer(new Slot(inventory, i, 8 + (i * 18), 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tEntity.isUseableByPlayer(player);
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
				if (!this.mergeItemStack(tempStack, 0, tEntity.getSizeInventory(), false)) {
					return null;
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

}
