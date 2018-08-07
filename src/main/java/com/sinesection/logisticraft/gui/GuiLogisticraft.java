package com.sinesection.logisticraft.gui;

import java.awt.Rectangle;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.sinesection.logisticraft.api.gui.GuiUtil;
import com.sinesection.logisticraft.api.gui.IGuiElement;
import com.sinesection.logisticraft.api.gui.events.GuiEvent;
import com.sinesection.logisticraft.api.gui.events.GuiEventDestination;
import com.sinesection.logisticraft.gui.elements.Window;
import com.sinesection.logisticraft.gui.widgets.TankWidget;
import com.sinesection.logisticraft.gui.widgets.Widget;
import com.sinesection.logisticraft.gui.widgets.WidgetManager;
import com.sinesection.logisticraft.render.ColorProperties;
import com.sinesection.logisticraft.render.LogisticraftResource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public abstract class GuiLogisticraft<C extends Container> extends GuiContainer implements IGuiSizable {
	protected final C container;

	public final ResourceLocation textureFile;
	protected final WidgetManager widgetManager;
	protected final TextLayoutHelper textLayout;
	protected final Window<?> window;

	protected GuiLogisticraft(String texture, C container) {
		this(new LogisticraftResource(texture), container);
	}

	protected GuiLogisticraft(ResourceLocation texture, C container) {
		super(container);

		this.widgetManager = new WidgetManager(this);
		this.window = new Window<>(xSize, ySize, this);

		this.textureFile = texture;

		this.container = container;

		this.textLayout = new TextLayoutHelper(this, ColorProperties.INSTANCE);
	}

	/* LEDGERS */
	@Override
	public void initGui() {
		super.initGui();

		this.window.init(guiLeft, guiTop);

	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		window.setSize(width, height);
		super.setWorldAndResolution(mc, width, height);
	}

	@Override
	public void updateScreen() {
		window.updateClient();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		window.setMousePosition(mouseX, mouseY);
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		// this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	public ColorProperties getFontColor() {
		return ColorProperties.INSTANCE;
	}

	public FontRenderer getFontRenderer() {
		return fontRendererObj;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		// / Handle ledger clicks
		widgetManager.handleMouseClicked(mouseX, mouseY, mouseButton);
		IGuiElement origin = (window.getMousedOverElement() == null) ? this.window : this.window.getMousedOverElement();
		window.postEvent(new GuiEvent.DownEvent(origin, mouseX, mouseY, mouseButton), GuiEventDestination.ALL);
	}

	// @Override
	// protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	// if (widgetManager.handleMouseRelease(mouseX, mouseY, mouseButton)) {
	// return;
	// }
	// IGuiElement origin = (window.getMousedOverElement() == null) ?
	// this.window : this.window.getMousedOverElement();
	// window.postEvent(new GuiEvent.UpEvent(origin, mouseX, mouseY,
	// mouseButton), GuiEventDestination.ALL);
	// super.mouseReleased(mouseX, mouseY, mouseButton);
	// }

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		if (keyCode == 1 || (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode() && this.window.getFocusedElement() == null)) {
			this.mc.thePlayer.closeScreen();
		}
		IGuiElement origin = (window.getFocusedElement() == null) ? this.window : this.window.getFocusedElement();
		window.postEvent(new GuiEvent.KeyEvent(origin, typedChar, keyCode), GuiEventDestination.ALL);
	}

	@Nullable
	public FluidStack getFluidStackAtPosition(int mouseX, int mouseY) {
		for (Widget widget : widgetManager.getWidgets()) {
			if (widget instanceof TankWidget && widget.isMouseOver(mouseX - guiLeft, mouseY - guiTop)) {
				TankWidget tankWidget = (TankWidget) widget;
				IFluidTank tank = tankWidget.getTank();
				if (tank != null) {
					return tank.getFluid();
				}
			}
		}
		return null;
	}

	@Nullable
	protected Slot getSlotAtPosition(int mouseX, int mouseY) {
		for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(k);

			if (isMouseOverSlot(slot, mouseX, mouseY)) {
				return slot;
			}
		}

		return null;
	}

	private boolean isMouseOverSlot(Slot par1Slot, int mouseX, int mouseY) {
		return isPointInRegion(par1Slot.xDisplayPosition, par1Slot.yDisplayPosition, 16, 16, mouseX, mouseY);
	}

	private boolean isPointInRegion(int x, int y, int w, int h, int mouseX, int mouseY) {
		return new Rectangle(x, y, w, h).contains(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		InventoryPlayer playerInv = mc.thePlayer.inventory;

		if (playerInv.getItemStack() == null) {
			GuiUtil.drawToolTips(this, widgetManager.getWidgets(), mouseX, mouseY);
			GuiUtil.drawToolTips(this, buttonList, mouseX, mouseY);
			GuiUtil.drawToolTips(this, inventorySlots.inventorySlots, mouseX, mouseY);
			window.drawTooltip(mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		drawBackground();

		widgetManager.updateWidgets(mouseX - guiLeft, mouseY - guiTop);

		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(guiLeft, guiTop, 0.0F);
			drawWidgets();
		}
		GL11.glPopMatrix();

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		window.draw(mouseX, mouseY);

		bindTexture(textureFile);
	}

	protected void drawBackground() {
		bindTexture(textureFile);

		// int x = (width - xSize) / 2;
		// int y = (height - ySize) / 2;
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	protected void drawWidgets() {
		widgetManager.drawWidgets();
	}

	protected void bindTexture(ResourceLocation texturePath) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		textureManager.bindTexture(texturePath);
	}

	public void setZLevel(float level) {
		this.zLevel = level;
	}

	@Override
	public int getSizeX() {
		return xSize;
	}

	@Override
	public int getSizeY() {
		return ySize;
	}

	@Override
	public int getGuiLeft() {
		return guiLeft;
	}

	@Override
	public int getGuiTop() {
		return guiTop;
	}

	@Override
	public Minecraft getMC() {
		return mc;
	}

	@Override
	public void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
		super.drawGradientRect(par1, par2, par3, par4, par5, par6);
	}

	public TextLayoutHelper getTextLayout() {
		return textLayout;
	}
}
