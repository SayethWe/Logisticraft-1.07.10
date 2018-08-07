package com.sinesection.logisticraft.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidTank;

public abstract class LogisticraftGuiContainer extends GuiContainer {

	public LogisticraftGuiContainer(Container container) {
		super(container);
	}

	@Override
	protected abstract void drawGuiContainerBackgroundLayer(float f, int i, int j);

	/**
	 * Draw a bar of fluid.
	 * 
	 * @param tank
	 *            Gives the fluid, amount, and capacity of tank.
	 * @param tx
	 * @param ty
	 * @param width
	 * @param height
	 * @param horizontal
	 */
	public void drawFluidTank(FluidTank tank, int x, int y, int width, int height, boolean horizontal) {
		if (tank == null || tank.getFluid() == null || tank.getFluid().getFluid() == null) {
			return;
		}
		IIcon icon = tank.getFluid().getFluid().getStillIcon();
		if (icon == null) {
			icon = tank.getFluid().getFluid().getIcon();
			if (icon == null) {
				return;
			}
		}

		int renderAmount = (int) Math.max(Math.min(height, tank.getFluidAmount() * height / tank.getCapacity()), 1);
		int posY = (int) (y + height - renderAmount);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		int color = tank.getFluid().getFluid().getColor(tank.getFluid());
		GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));

		GL11.glEnable(GL11.GL_BLEND);
		for (int i = 0; i < width; i += 16) {
			for (int j = 0; j < renderAmount; j += 16) {
				int drawWidth = (int) Math.min(width - i, 16);
				int drawHeight = Math.min(renderAmount - j, 16);

				int drawX = (int) (x + i);
				int drawY = posY + j;

				double minU = icon.getMinU();
				double maxU = icon.getMaxU();
				double minV = icon.getMinV();
				double maxV = icon.getMaxV();

				Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(drawX, drawY + drawHeight, 0, minU, minV + (maxV - minV) * drawHeight / 16F);
				tessellator.addVertexWithUV(drawX + drawWidth, drawY + drawHeight, 0, minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F);
				tessellator.addVertexWithUV(drawX + drawWidth, drawY, 0, minU + (maxU - minU) * drawWidth / 16F, minV);
				tessellator.addVertexWithUV(drawX, drawY, 0, minU, minV);
				tessellator.draw();
			}
		}
		GL11.glDisable(GL11.GL_BLEND);
	}
}
