package com.sinesection.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundList.SoundEntry;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundEvent;

public class SoundUtils {
	@SideOnly(Side.CLIENT)
	public static void playButtonClick() {
		//playSoundEvent(SoundEvents.UI_BUTTON_CLICK);
	}

	@SideOnly(Side.CLIENT)
	public static void playSoundEvent(SoundEvent soundIn) {
		playSoundEvent(soundIn, 1.0f);
	}

	@SideOnly(Side.CLIENT)
	public static void playSoundEvent(SoundEvent soundIn, float pitchIn) {
		Minecraft minecraft = Minecraft.getMinecraft();
		SoundHandler soundHandler = minecraft.getSoundHandler();
		//PositionedSoundRecord sound = PositionedSoundRecord.getMasterRecord(soundIn, pitchIn);
		//soundHandler.playSound(sound);
	}
}