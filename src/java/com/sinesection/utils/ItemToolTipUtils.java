package com.sinesection.utils;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemToolTipUtils {
	@SideOnly(Side.CLIENT)
	public static void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, EnumRarity flag) {
		String unlocalizedName = stack.getUnlocalizedName();
		String tooltipKey = unlocalizedName + ".tooltip";
		if (I18n.format(tooltipKey) != null) {
			String tooltipInfo = I18n.format(tooltipKey);
			Minecraft minecraft = Minecraft.getMinecraft();
			List<String> tooltipInfoWrapped = minecraft.fontRenderer.listFormattedStringToWidth(tooltipInfo, 150);
			tooltip.addAll(tooltipInfoWrapped);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void addShiftInformation(ItemStack stack, World world, List<String> tooltip) {
		tooltip.add(EnumChatFormatting.ITALIC.toString() + '<' + I18n.format("for.gui.tooltip.tmi") + '>');
	}

	@SideOnly(Side.CLIENT)
	public static List<String> getInformation(ItemStack stack) {
		Minecraft minecraft = Minecraft.getMinecraft();
		return getInformation(stack, minecraft.thePlayer, minecraft.gameSettings.advancedItemTooltips);
	}

	@SideOnly(Side.CLIENT)
	public static List<String> getInformation(ItemStack stack, EntityPlayer player, boolean advancedTooltips) {
		if (stack == null) {
			return Collections.emptyList();
		}
		List<String> tooltip = stack.getTooltip(player, advancedTooltips);
		for (int i = 0; i < tooltip.size(); ++i) {
			if (i == 0) {
				tooltip.set(i, stack.getRarity().rarityColor + tooltip.get(i));
			} else {
				tooltip.set(i, EnumChatFormatting.GRAY + tooltip.get(i));
			}
		}
		return tooltip;
	}
}