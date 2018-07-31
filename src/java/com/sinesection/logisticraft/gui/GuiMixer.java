package com.sinesection.logisticraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.sinesection.logisticraft.Constants;
import com.sinesection.logisticraft.Main;
import com.sinesection.logisticraft.block.tileentity.TileEntityFractionator;
import com.sinesection.logisticraft.block.tileentity.TileEntityMixer;
import com.sinesection.logisticraft.container.ContainerFractionator;
import com.sinesection.logisticraft.network.ContainerMixer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiMixer extends GuiContainer {

	public static final ResourceLocation guiBgTexture = new ResourceLocation(Constants.MOD_ID, "textures/gui/guiFractionator.png");

	private static final int TEXT_COLOR = 4210752;

	private static final int GUI_WIDTH = 176;
	private static final int GUI_HEIGHT = 166;

	public TileEntityMixer tEntity;
	private float mouseX, mouseY;

	public GuiMixer(InventoryPlayer inventory, TileEntityMixer tEntity) {
		super(new ContainerMixer(inventory, tEntity));
		this.tEntity = tEntity;
		this.xSize = GUI_WIDTH;
		this.ySize = GUI_HEIGHT;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String displayName = this.tEntity.hasCustomInventoryName() ? this.tEntity.getInventoryName() : I18n.format(this.tEntity.getInventoryName());

		this.fontRendererObj.drawString(displayName, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayName) / 2, 6, TEXT_COLOR);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, TEXT_COLOR);

		// TODO TANKS
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		// TODO Everything else
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		this.mouseX = (float) mouseX;
		this.mouseY = (float) mouseY;
		super.drawScreen(mouseX, mouseY, par3);
	}

}
