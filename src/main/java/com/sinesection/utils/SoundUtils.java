package com.sinesection.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;

public class SoundUtils {
	@SideOnly(Side.CLIENT)
	public static void playButtonClick() {
		playSound("gui.button.press");
	}

	@SideOnly(Side.CLIENT)
	public static void playSound(String soundIn) {
		playSound(soundIn, 1.0f);
	}

	@SideOnly(Side.CLIENT)
	public static void playSound(String soundIn, float pitchIn) {
		Minecraft minecraft = Minecraft.getMinecraft();
		SoundHandler soundHandler = minecraft.getSoundHandler();
		PositionedSoundRecord sound = PositionedSoundRecord.func_147674_a(new ResourceLocation(soundIn), pitchIn);
		soundHandler.playSound(sound);
	}
}