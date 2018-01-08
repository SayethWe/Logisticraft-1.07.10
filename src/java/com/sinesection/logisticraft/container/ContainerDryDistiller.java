package com.sinesection.logisticraft.container;

import com.sinesection.logisticraft.tileentity.TileEntityDryDistiller;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerDryDistiller extends GuiContainer {

	public ContainerDryDistiller(InventoryPlayer inventory, TileEntityDryDistiller tEntity) {
		super(null);
	}

	public boolean canInteractWith(EntityPlayer player) {
		return false;
	}

	protected void drawGuiContainerBackgroundLayer(float i, int f, int j) {
		
	}

}
