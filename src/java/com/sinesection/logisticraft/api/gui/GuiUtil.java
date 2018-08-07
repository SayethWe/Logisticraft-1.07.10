package com.sinesection.logisticraft.api.gui;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.sinesection.logisticraft.gui.GuiLogisticraft;
import com.sinesection.logisticraft.gui.IGuiSizable;
import com.sinesection.logisticraft.gui.tooltips.IToolTipProvider;
import com.sinesection.logisticraft.gui.tooltips.ToolTip;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class GuiUtil {
	public static void drawItemStack(GuiLogisticraft<?> gui, ItemStack stack, int xPos, int yPos) {
		drawItemStack(gui.getFontRenderer(), stack, xPos, yPos);
	}

	public static void drawItemStack(FontRenderer fontRenderer, ItemStack stack, int xPos, int yPos) {
		FontRenderer font = null;
		if (stack != null) {
			font = stack.getItem().getFontRenderer(stack);
		}
		if (font == null) {
			font = fontRenderer;
		}

		RenderItem itemRender = RenderItem.getInstance();
		itemRender.renderItemAndEffectIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, xPos, yPos);
		itemRender.renderItemOverlayIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, xPos, yPos, null);
	}

	public static void drawToolTips(IGuiSizable gui, @Nullable IToolTipProvider provider, ToolTip toolTips, int mouseX, int mouseY) {
		List<String> lines = toolTips.getLines();
		if (!lines.isEmpty()) {
			GL11.glPushMatrix();
			if (provider == null || provider.isRelativeToGui()) {
				GL11.glTranslatef(-gui.getGuiLeft(), -gui.getGuiTop(), 0);
			}
			// TODO Shits fucked, bro.
			// ScaledResolution scaledresolution = new
			// ScaledResolution(gui.getMC());
			// GuiUtils.drawHoveringText(lines, mouseX, mouseY,
			// scaledresolution.getScaledWidth(),
			// scaledresolution.getScaledHeight(), -1,
			// gui.getMC().fontRenderer);
			GL11.glPopMatrix();
		}
	}

	public static void drawToolTips(IGuiSizable gui, Collection<?> objects, int mouseX, int mouseY) {
		for (Object obj : objects) {
			if (!(obj instanceof IToolTipProvider)) {
				continue;
			}
			IToolTipProvider provider = (IToolTipProvider) obj;
			if (!provider.isToolTipVisible()) {
				continue;
			}
			int mX = mouseX;
			int mY = mouseY;
			if (provider.isRelativeToGui()) {
				mX -= gui.getGuiLeft();
				mY -= gui.getGuiTop();
			}
			ToolTip tips = provider.getToolTip(mX, mY);
			if (tips == null) {
				continue;
			}
			boolean mouseOver = provider.isMouseOver(mX, mY);
			tips.onTick(mouseOver);
			if (mouseOver && tips.isReady()) {
				tips.refresh();
				drawToolTips(gui, provider, tips, mouseX, mouseY);
			}
		}
	}
}