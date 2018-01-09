package com.sinesection.logisticraft.container;

import com.sinesection.logisticraft.tileentity.TileEntityDryDistiller;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerDryDistiller extends Container {
	
	public TileEntityDryDistiller tEntity;
	
	/** Last time left for fuel to be used up. (in ticks) */
	public int lastBurnTime;
	/** Time it takes to burn the current item in slot 1. (in ticks) */
	public int lastItemBurnTime;

	/** Time left to process the item in slot 0. (in ticks) */
	public int lastProcessTime;

	public ContainerDryDistiller(InventoryPlayer inventory, TileEntityDryDistiller tEntity) {
		this.tEntity = tEntity;
		
		this.addSlotToContainer(new Slot(tEntity, 0, 56, 25)); // Input
		this.addSlotToContainer(new Slot(tEntity, 1, 8, 54)); // Fuel
		
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
	public void addCraftingToCrafters(ICrafting crafting) {
		super.addCraftingToCrafters(crafting);
		crafting.sendProgressBarUpdate(this, 0, this.tEntity.processTime);
		crafting.sendProgressBarUpdate(this, 1, this.tEntity.burnTime);
		crafting.sendProgressBarUpdate(this, 2, this.tEntity.currentItemBurnTime);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
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
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return null;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tEntity.isUseableByPlayer(player);
	}

}
