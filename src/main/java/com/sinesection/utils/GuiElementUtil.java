package com.sinesection.utils;

import com.sinesection.logisticraft.api.gui.style.ITextStyle;

import net.minecraft.util.EnumChatFormatting;

public class GuiElementUtil {
	private GuiElementUtil() {
	}

	public static String getFormattedString(ITextStyle style, String rawText) {
		StringBuilder modifiers = new StringBuilder();
		if (style.isBold()) {
			modifiers.append(EnumChatFormatting.BOLD);
		}
		if (style.isItalic()) {
			modifiers.append(EnumChatFormatting.ITALIC);
		}
		if (style.isUnderlined()) {
			modifiers.append(EnumChatFormatting.UNDERLINE);
		}
		if (style.isStrikethrough()) {
			modifiers.append(EnumChatFormatting.STRIKETHROUGH);
		}
		if (style.isObfuscated()) {
			modifiers.append(EnumChatFormatting.OBFUSCATED);
		}
		modifiers.append(rawText);
		return modifiers.toString();
	}

}