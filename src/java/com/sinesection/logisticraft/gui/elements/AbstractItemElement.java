package com.sinesection.logisticraft.gui.elements;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.sinesection.logisticraft.api.gui.GuiUtil;
import com.sinesection.utils.ItemToolTipUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public abstract class AbstractItemElement extends GuiElement {

	public AbstractItemElement(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}

	public AbstractItemElement(int xPos, int yPos) {
		super(xPos, yPos, 16, 16);
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		ItemStack itemStack = getStack();
		if (itemStack != null) {
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GuiUtil.drawItemStack(Minecraft.getMinecraft().fontRenderer, itemStack, 0, 0);
			RenderHelper.disableStandardItemLighting();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public List<String> getTooltip(int mouseX, int mouseY) {
		ItemStack itemStack = getStack();
		return ItemToolTipUtils.getInformation(itemStack);
	}

	@Override
	public boolean hasTooltip() {
		return true;
	}

	protected abstract ItemStack getStack();
}