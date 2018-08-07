package com.sinesection.logisticraft.gui.buttons;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.sinesection.logisticraft.Constants;
import com.sinesection.logisticraft.gui.tooltips.IToolTipProvider;
import com.sinesection.logisticraft.gui.tooltips.ToolTip;
import com.sinesection.logisticraft.render.LogisticraftResource;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiBetterButton extends GuiButton implements IToolTipProvider {

	public static final ResourceLocation TEXTURE = new LogisticraftResource(Constants.TEXTURE_PATH_GUI + "/buttons.png");
	protected IButtonTextureSet texture;
	@Nullable
	private ToolTip toolTip;
	private boolean useTexWidth = false;

	public GuiBetterButton(int id, int x, int y, IButtonTextureSet texture) {
		super(id, x, y, texture.getWidth(), texture.getHeight(), "");
		this.texture = texture;
		useTexWidth = true;
	}

	public GuiBetterButton setTexture(IButtonTextureSet texture) {
		this.texture = texture;
		width = texture.getWidth();
		height = texture.getHeight();
		return this;
	}

	public GuiBetterButton setUseTextureWidth() {
		useTexWidth = true;
		return this;
	}

	public GuiBetterButton setGuiWidth(int width) {
		this.width = width;
		useTexWidth = false;
		return this;
	}

	public GuiBetterButton setLabel(String label) {
		this.displayString = label;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return texture.getHeight();
	}

	public int getTextColor(boolean mouseOver) {
		if (!enabled) {
			return 0xffa0a0a0;
		} else if (mouseOver) {
			return 0xffffa0;
		} else {
			return 0xe0e0e0;
		}
	}

	public boolean isMouseOverButton(int mouseX, int mouseY) {
		return mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + getWidth() && mouseY < yPosition + getHeight();
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if (!visible) {
			return;
		}
		FontRenderer fontrenderer = minecraft.fontRenderer;
		minecraft.getTextureManager().bindTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int xOffset = texture.getX();
		int yOffset = texture.getY();
		int h = texture.getHeight();
		int w = texture.getWidth();
		boolean mouseOver = isMouseOverButton(mouseX, mouseY);
		int hoverState = getHoverState(mouseOver);
		if (useTexWidth) {
			drawTexturedModalRect(xPosition, yPosition, xOffset, yOffset + hoverState * h, w, h);
		} else {
			drawTexturedModalRect(xPosition, yPosition, xOffset, yOffset + hoverState * h, width / 2, h);
			drawTexturedModalRect(xPosition + width / 2, yPosition, xOffset + w - width / 2, yOffset + hoverState * h, width / 2, h);
		}
		mouseDragged(minecraft, mouseX, mouseY);
		drawCenteredString(fontrenderer, displayString, xPosition + getWidth() / 2, yPosition + (h - 8) / 2, getTextColor(mouseOver));
	}

	@Override
	public ToolTip getToolTip(int mouseX, int mouseY) {
		return toolTip;
	}

	public void setToolTip(ToolTip tips) {
		this.toolTip = tips;
	}

	@Override
	public boolean isToolTipVisible() {
		return visible;
	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
		return isMouseOverButton(mouseX, mouseY);
	}
}