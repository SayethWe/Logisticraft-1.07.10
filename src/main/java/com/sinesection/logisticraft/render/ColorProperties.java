package com.sinesection.logisticraft.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sinesection.utils.Log;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

@SideOnly(Side.CLIENT)
public class ColorProperties implements IResourceManagerReloadListener {

	public static final ColorProperties INSTANCE;

	static {
		INSTANCE = new ColorProperties();
	}

	private final Properties defaultMappings = new Properties();
	private final Properties mappings = new Properties();

	private ColorProperties() {
		((IReloadableResourceManager) Minecraft.getMinecraft().getTextureManager()).registerReloadListener(this);
	}

	public synchronized int get(String key) {
		return Integer.parseInt(mappings.getProperty(key, defaultMappings.getProperty(key, "d67fff")), 16);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		try {
			InputStream defaultFontStream = ColorProperties.class.getResourceAsStream("/config/logisticraft/color.properties");
			mappings.load(defaultFontStream);
			defaultMappings.load(defaultFontStream);

			defaultFontStream.close();
		} catch (IOException e) {
			Log.error("Failed to load colors.properties.", e);
		}
	}

}