package com.sinesection.logisticraft.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class Drawable {
	/* Final Attributes */
	// Position on the Texture
	public final int u;
	public final int v;
	// Rectangle Size
	public final int uWidth;
	public final int vHeight;
	// Texture
	public final ResourceLocation textureLocation;
	// Texture Size
	private final int textureWidth;
	private final int textureHeight;

	public Drawable(ResourceLocation textureLocation, int u, int v, int uWidth, int vHeight) {
		this(textureLocation, u, v, uWidth, vHeight, 256, 256);
	}

	public Drawable(ResourceLocation textureLocation, int u, int v, int uWidth, int vHeight, int textureWidth, int textureHeight) {
		this.u = u;
		this.v = v;
		this.uWidth = uWidth;
		this.vHeight = vHeight;
		this.textureLocation = textureLocation;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	public void draw(int xOffset, int yOffset) {
		draw(xOffset, yOffset, uWidth, vHeight);
	}

	public void draw(int xOffset, int yOffset, int width, int height) {
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		textureManager.bindTexture(textureLocation);

		// Enable correct lighting.
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiUtils.drawContinuousTexturedBox(xOffset, yOffset, u, v, uWidth, vHeight, width, height, textureWidth, textureHeight);
	}
}