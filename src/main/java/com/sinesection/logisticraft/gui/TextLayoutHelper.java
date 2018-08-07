package com.sinesection.logisticraft.gui;

import org.lwjgl.opengl.GL11;

import com.sinesection.logisticraft.render.ColorProperties;

public class TextLayoutHelper {
	private static final int LINE_HEIGHT = 12;

	private final GuiLogisticraft<?> guiLogisticraft;
	private final int defaultFontColor;

	public int column0;
	public int column1;
	public int column2;
	public int line;

	public TextLayoutHelper(GuiLogisticraft<?> guiLogisticraft, ColorProperties fontColor) {
		this.guiLogisticraft = guiLogisticraft;
		this.defaultFontColor = fontColor.get("gui.screen");
	}

	public void startPage() {
		line = LINE_HEIGHT;
		GL11.glPushMatrix();
	}

	public void startPage(int column0, int column1) {
		startPage(column0, column1, 0);
	}

	public void startPage(int column0, int column1, int column2) {
		this.column0 = column0;
		this.column1 = column1;
		this.column2 = column2;

		startPage();
	}

	public int getLineY() {
		return line;
	}

	public void newLine() {
		line += LINE_HEIGHT;
	}

	public void newLineCompressed() {
		line += LINE_HEIGHT - 2;
	}

	public void newLine(int lineHeight) {
		line += lineHeight;
	}

	public void endPage() {
		GL11.glPopMatrix();
	}

	public void drawRow(String text0, String text1, String text2, int colour0, int colour1, int colour2) {
		drawLine(text0, column0, colour0);
		drawLine(text1, column1, colour1);
		drawLine(text2, column2, colour2);
	}

	public void drawLine(String text, int x) {
		drawLine(text, x, defaultFontColor);
	}

	public void drawSplitLine(String text, int x, int maxWidth) {
		drawSplitLine(text, x, maxWidth, defaultFontColor);
	}

	public void drawCenteredLine(String text, int x, int color) {
		drawCenteredLine(text, x, guiLogisticraft.getSizeX(), color);
	}

	public void drawCenteredLine(String text, int x, int width, int color) {
		drawCenteredLine(text, x, 0, width, color);
	}

	public void drawCenteredLine(String text, int x, int y, int width, int color) {
		guiLogisticraft.getFontRenderer().drawString(text, guiLogisticraft.getGuiLeft() + x + getCenteredOffset(text, width), guiLogisticraft.getGuiTop() + y + line, color);
	}

	public void drawLine(String text, int x, int color) {
		drawLine(text, x, 0, color);
	}

	public void drawLine(String text, int x, int y, int color) {
		guiLogisticraft.getFontRenderer().drawString(text, guiLogisticraft.getGuiLeft() + x, guiLogisticraft.getGuiTop() + y + line, color);
	}

	public void drawSplitLine(String text, int x, int maxWidth, int color) {
		guiLogisticraft.getFontRenderer().drawSplitString(text, guiLogisticraft.getGuiLeft() + x, guiLogisticraft.getGuiTop() + line, maxWidth, color);
	}

	public int getCenteredOffset(String string) {
		return getCenteredOffset(string, guiLogisticraft.getSizeX());
	}

	public int getCenteredOffset(String string, int xWidth) {
		return (xWidth - guiLogisticraft.getFontRenderer().getStringWidth(string)) / 2;
	}
}
