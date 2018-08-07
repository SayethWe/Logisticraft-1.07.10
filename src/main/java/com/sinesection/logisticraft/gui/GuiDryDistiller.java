package com.sinesection.logisticraft.gui;

import org.lwjgl.opengl.GL11;

import com.sinesection.logisticraft.Constants;
import com.sinesection.logisticraft.block.tileentity.TileEntityDryDistiller;
import com.sinesection.logisticraft.container.ContainerDryDistiller;
import com.sinesection.logisticraft.render.LogisticraftResource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDryDistiller extends LogisticraftGuiContainer {

	public static final ResourceLocation guiBgTexture = new LogisticraftResource(Constants.TEXTURE_PATH_GUI + "/guiDistiller.png");

	public TileEntityDryDistiller tEntity;

	private final int textColor = 4210752;

	public GuiDryDistiller(InventoryPlayer inventory, TileEntityDryDistiller tEntity) {
		super(new ContainerDryDistiller(inventory, tEntity));
		this.tEntity = tEntity;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String displayName = this.tEntity.hasCustomInventoryName() ? this.tEntity.getInventoryName() : I18n.format(this.tEntity.getInventoryName());

		this.fontRendererObj.drawString(displayName, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayName) / 2, 6, textColor);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, textColor);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1f, 1f, 1f, 1f);

		Minecraft.getMinecraft().getTextureManager().bindTexture(guiBgTexture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if (tEntity.isBurning()) {
			int k = this.tEntity.getBurnTimeScaled(16);
			drawTexturedModalRect(guiLeft + 7, guiTop + 36 + 16 - k, xSize, 13 + 16 - k, 18, k);
		}

		if (tEntity.isRunning()) {
			int k = this.tEntity.getProgressScaled(51);
			drawTexturedModalRect(guiLeft + 59, guiTop + 44, xSize, 0, k, 13);
		}
	}

}
