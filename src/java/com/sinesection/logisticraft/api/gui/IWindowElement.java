package com.sinesection.logisticraft.api.gui;

import javax.annotation.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
/**
 * The window element is the root element of the containment hierarchy. Its the only element the gui interacts with
 * directly.
 */
@SideOnly(Side.CLIENT)
public interface IWindowElement extends IElementGroup {
	/**
	 * Returns the mouse position.
	 */
	int getMouseX();

	/**
	 * Returns the mouse position.
	 */
	int getMouseY();

	/**
	 * Returns the mouse position relative to the given element.
	 */
	int getRelativeMouseX(@Nullable IGuiElement element);

	/**
	 * Returns the mouse position relative to the given element.
	 */
	int getRelativeMouseY(@Nullable IGuiElement element);

	/**
	 * Returns the current screen width.
	 */
	int getScreenWidth();

	/**
	 * Returns the current screen height.
	 */
	int getScreenHeight();

	/**
	 * Returns the current gui height.
	 */
	int getGuiHeight();

	/**
	 * Returns the current gui width.
	 */
	int getGuiWidth();

	int getGuiLeft();

	int getGuiTop();

	TextureManager getTextureManager();

	FontRenderer getFontRenderer();

	@Nullable
	IGuiElement getMousedOverElement();

	@Nullable
	IGuiElement getDraggedElement();

	@Nullable
	IGuiElement getFocusedElement();

	boolean isMouseOver(IGuiElement element);

	boolean isDragged(IGuiElement element);

	boolean isFocused(IGuiElement element);
}