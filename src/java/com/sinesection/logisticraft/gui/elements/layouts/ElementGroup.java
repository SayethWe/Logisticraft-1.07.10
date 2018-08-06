package com.sinesection.logisticraft.gui.elements.layouts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.sinesection.logisticraft.api.gui.GuiConstants;
import com.sinesection.logisticraft.api.gui.GuiElementAlignment;
import com.sinesection.logisticraft.api.gui.IElementGroup;
import com.sinesection.logisticraft.api.gui.IElementLayoutHelper;
import com.sinesection.logisticraft.api.gui.IGuiElement;
import com.sinesection.logisticraft.api.gui.IItemElement;
import com.sinesection.logisticraft.api.gui.ILabelElement;
import com.sinesection.logisticraft.api.gui.ITextElement;
import com.sinesection.logisticraft.api.gui.style.ITextStyle;
import com.sinesection.logisticraft.gui.Drawable;
import com.sinesection.logisticraft.gui.elements.GuiElement;
import com.sinesection.logisticraft.gui.elements.LabelElement;
import com.sinesection.logisticraft.gui.elements.SplitTextElement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;


@SideOnly(Side.CLIENT)
public abstract class ElementGroup extends GuiElement implements IElementGroup {
	protected final List<IGuiElement> elements = new ArrayList<>();

	protected ElementGroup(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}

	public <E extends IGuiElement> E add(E element) {
		elements.add(element);
		element.setParent(this);
		element.onCreation();
		return element;
	}

	public <E extends IGuiElement> E remove(E element) {
		elements.remove(element);
		element.onDeletion();
		return element;
	}

	public void clear() {
		for (IGuiElement element : new ArrayList<>(elements)) {
			remove(element);
		}
	}

	@Override
	public List<IGuiElement> getElements() {
		return elements;
	}

	@Override
	public void drawElement(int mouseX, int mouseY) {
		int mX = mouseX - getX();
		int mY = mouseY - getY();
		elements.forEach(element -> element.draw(mX, mY));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateClient() {
		if (!isVisible()) {
			return;
		}
		onUpdateClient();
		for (IGuiElement widget : getElements()) {
			widget.updateClient();
		}
	}

	@Nullable
	@Override
	public IGuiElement getLastElement() {
		return elements.isEmpty() ? null : elements.get(elements.size() - 1);
	}

	public DrawableElement drawable(Drawable drawable) {
		return add(new DrawableElement(drawable));
	}

	public DrawableElement drawable(int x, int y, Drawable drawable) {
		return add(new DrawableElement(x, y, drawable));
	}

	@Override
	public IItemElement item(int xPos, int yPos, ItemStack itemStack) {
		IItemElement element = new ItemElement(xPos, yPos, itemStack);
		add(element);
		return element;
	}

	@Override
	public ILabelElement label(String text) {
		return label(text, GuiConstants.DEFAULT_STYLE);
	}

	@Override
	public ILabelElement label(String text, ITextStyle style) {
		return label(text, GuiElementAlignment.TOP_LEFT, style);
	}

	@Override
	public ILabelElement label(String text, GuiElementAlignment align) {
		return label(text, align, GuiConstants.DEFAULT_STYLE);
	}

	@Override
	public ILabelElement label(String text, GuiElementAlignment align, ITextStyle textStyle) {
		return label(text, -1, 12, align, textStyle);
	}

	@Override
	public ILabelElement label(String text, int width, int height, GuiElementAlignment align, ITextStyle textStyle) {
		return label(text, 0, 0, width, height, align, textStyle);
	}

	@Override
	public ILabelElement label(String text, int x, int y, int width, int height, GuiElementAlignment align, ITextStyle textStyle) {
		return add(new LabelElement(x, y, width, height, text, align, textStyle));
	}

	@Override
	public ITextElement splitText(String text, int width) {
		return splitText(text, width, GuiConstants.DEFAULT_STYLE);
	}

	@Override
	public ITextElement splitText(String text, int width, ITextStyle textStyle) {
		return splitText(text, width, GuiElementAlignment.TOP_LEFT, textStyle);
	}

	@Override
	public ITextElement splitText(String text, int width, GuiElementAlignment align, ITextStyle textStyle) {
		return splitText(text, 0, 0, width, align, textStyle);
	}

	@Override
	public ITextElement splitText(String text, int x, int y, int width, GuiElementAlignment align, ITextStyle textStyle) {
		return add(new SplitTextElement(x, y, width, text, align, textStyle));
	}

	@Override
	public AbstractElementLayout vertical(int xPos, int yPos, int width) {
		return add(new VerticalLayout(xPos, yPos, width));
	}

	@Override
	public AbstractElementLayout vertical(int width) {
		return add(new VerticalLayout(0, 0, width));
	}

	@Override
	public AbstractElementLayout horizontal(int xPos, int yPos, int height) {
		return add(new HorizontalLayout(xPos, yPos, height));
	}

	@Override
	public AbstractElementLayout horizontal(int height) {
		return add(new HorizontalLayout(0, 0, height));
	}

	@Override
	public ElementGroup pane(int xPos, int yPos, int width, int height) {
		return add(new PaneLayout(xPos, yPos, width, height));
	}

	@Override
	public ElementGroup pane(int width, int height) {
		return add(new PaneLayout(0, 0, width, height));
	}

	@Override
	public ElementLayoutHelper layoutHelper(IElementLayoutHelper.LayoutFactory layoutFactory, int width, int height) {
		return new ElementLayoutHelper(layoutFactory, width, height, this);
	}
}
