package com.sinesection.logisticraft.gui.elements;

import java.util.function.Consumer;

import com.sinesection.logisticraft.api.gui.events.GuiEvent;
import com.sinesection.logisticraft.gui.Drawable;
import com.sinesection.logisticraft.gui.buttons.StandardButtonTextureSets;
import com.sinesection.utils.SoundUtils;

public class ButtonElement extends GuiElement {
	/* Attributes - Final */
	private final Consumer<ButtonElement> onClicked;
	private final Drawable[] textures = new Drawable[3];

	/* Attributes - State */
	private boolean enabled = true;

	public ButtonElement(int xPos, int yPos, Drawable drawable, Consumer<ButtonElement> onClicked) {
		super(xPos, yPos, drawable.uWidth, drawable.vHeight);
		this.onClicked = onClicked;
		for (int i = 0; i < 3; i++) {
			textures[i] = new Drawable(drawable.textureLocation, drawable.u, drawable.v + drawable.vHeight * i, drawable.uWidth, drawable.vHeight);
		}
		addSelfEventHandler(GuiEvent.DownEvent.class, event -> {
			if (!enabled) {
				return;
			}
			onPressed();
			SoundUtils.playButtonClick();
		});
	}

	public ButtonElement(int xPos, int yPos, StandardButtonTextureSets textureSets, Consumer<ButtonElement> onClicked) {
		super(xPos, yPos, textureSets.getWidth(), textureSets.getHeight());
		this.onClicked = onClicked;
		for (int i = 0; i < 3; i++) {
			textures[i] = new Drawable(textureSets.getTexture(), textureSets.getX(), textureSets.getY() + textureSets.getHeight() * i, textureSets.getWidth(), textureSets.getHeight());
		}
		addSelfEventHandler(GuiEvent.DownEvent.class, event -> {
			if (!enabled) {
				return;
			}
			onPressed();
			SoundUtils.playButtonClick();
		});
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		boolean mouseOver = isMouseOver();
		int hoverState = getHoverState(mouseOver);
		Drawable drawable = textures[hoverState];
		drawable.draw(0, 0);
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	protected int getHoverState(boolean mouseOver) {
		int i = 1;

		if (!this.enabled) {
			i = 0;
		} else if (mouseOver) {
			i = 2;
		}

		return i;
	}

	public void onPressed() {
		onClicked.accept(this);
	}

}