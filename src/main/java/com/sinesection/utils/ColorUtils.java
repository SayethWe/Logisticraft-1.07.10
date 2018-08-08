package com.sinesection.utils;

import org.lwjgl.util.Color;

public class ColorUtils {

	public static Color fromRGB(int rgb) {
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = (rgb >> 0) & 0xFF;
		return new Color(r, g, b);
	}

}
