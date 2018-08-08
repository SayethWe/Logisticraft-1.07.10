package com.sinesection.logisticraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.sinesection.logisticraft.Constants;
import com.sinesection.logisticraft.block.tileentity.TileEntityFractionator;
import com.sinesection.logisticraft.container.ContainerFractionator;
import com.sinesection.logisticraft.render.LogisticraftResource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFractionator extends LogisticraftGuiContainer {

	public static final ResourceLocation guiBgTexture = new LogisticraftResource(Constants.TEXTURE_PATH_GUI + "/guiFractionator.png");

	private static final int TEXT_COLOR = 4210752;

	private static final int GUI_WIDTH = 176;
	private static final int GUI_HEIGHT = 166;

	private static final int PROGRESS_X = 35;
	private static final int PROGRESS_Y = 44;
	private static final int PROGRESS_U = 0;
	private static final int PROGRESS_V = 0;
	private static final int PROGRESS_WIDTH = 51;
	private static final int PROGRESS_HEIGHT = 13;
	private static final int BURNER_X = 7;
	private static final int BURNER_Y = 36;
	private static final int BURNER_U = 0;
	private static final int BURNER_V = PROGRESS_V + PROGRESS_HEIGHT;
	private static final int BURNER_WIDTH = 18;
	private static final int BURNER_HEIGHT = 16;
	private static final int TANK_X = 130;
	private static final int TANK_Y = 18;
	private static final int TANK_WIDTH = 16;
	private static final int TANK_HEIGHT = 52;
	private static final int TANK_OVERLAY_U = 0;
	private static final int TANK_OVERLAY_V = BURNER_V + BURNER_HEIGHT;

	public TileEntityFractionator tEntity;
	private float mouseX, mouseY;

	public GuiFractionator(InventoryPlayer inventory, TileEntityFractionator tEntity) {
		super(new ContainerFractionator(inventory, tEntity));
		this.tEntity = tEntity;
		this.xSize = GUI_WIDTH;
		this.ySize = GUI_HEIGHT;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String displayName = this.tEntity.hasCustomInventoryName() ? this.tEntity.getInventoryName() : I18n.format(this.tEntity.getInventoryName());

		this.fontRendererObj.drawString(displayName, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayName) / 2, 6, TEXT_COLOR);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, TEXT_COLOR);

		int tankX = guiLeft + TANK_X;
		int tankY = guiTop + TANK_Y;
		if (mouseX >= tankX && mouseX <= tankX + TANK_WIDTH) {
			if (mouseY >= tankY && mouseY <= tankY + TANK_HEIGHT) {
				List<String> tankTooltip = new ArrayList<String>();
				if (this.tEntity.getOutputTank().getFluid() == null || this.tEntity.getOutputTank().getFluidAmount() == 0)
					tankTooltip.add("§7§o" + I18n.format("container.guiFractionator.tankEmpty"));
				else {
					tankTooltip.add("§o" + this.tEntity.getOutputTank().getFluid().getLocalizedName());
					tankTooltip.add("§7§o" + this.tEntity.getOutputTank().getFluidAmount() + "mb / " + this.tEntity.getOutputTank().getCapacity() + "mb");
				}
				int k = (this.width - this.xSize) / 2; // X axis on GUI
				int l = (this.height - this.ySize) / 2; // Y axis on GUI
				this.drawHoveringText(tankTooltip, (int) mouseX - k, (int) mouseY - l, this.fontRendererObj);
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1f, 1f, 1f, 1f);

		Minecraft.getMinecraft().getTextureManager().bindTexture(guiBgTexture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize); // BG

		if (tEntity.isBurning()) {
			int k = this.tEntity.getBurnTimeScaled(BURNER_HEIGHT);
			drawTexturedModalRect(guiLeft + BURNER_X, guiTop + BURNER_Y + (BURNER_HEIGHT - k), xSize + BURNER_U, (BURNER_HEIGHT - k) + BURNER_V, BURNER_WIDTH, k); // Burner
		}

		if (tEntity.isRunning()) {
			int k = this.tEntity.getProgressScaled(PROGRESS_WIDTH);
			drawTexturedModalRect(guiLeft + PROGRESS_X, guiTop + PROGRESS_Y, xSize + PROGRESS_U, PROGRESS_V, k, PROGRESS_HEIGHT); // Progress
																																	// //
																																	// Bar
		}

		if (tEntity.getOutputTank().getFluidAmount() > 0) {
			drawFluidTank(this.tEntity.getOutputTank(), guiLeft + TANK_X, guiTop + TANK_Y, TANK_WIDTH, TANK_HEIGHT, false); // Tank
			// Liquid
		}

		Minecraft.getMinecraft().getTextureManager().bindTexture(guiBgTexture);
		drawTexturedModalRect(guiLeft + TANK_X, guiTop + TANK_Y, xSize + TANK_OVERLAY_U, TANK_OVERLAY_V, TANK_WIDTH, TANK_HEIGHT); // Tank
		// Overlay
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		this.mouseX = (float) mouseX;
		this.mouseY = (float) mouseY;
		super.drawScreen(mouseX, mouseY, par3);
	}

}
