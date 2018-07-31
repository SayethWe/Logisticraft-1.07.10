package com.sinesection.utils;


import javax.annotation.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class ItemToolTipUtils {
	@SideOnly(Side.CLIENT)
	public static void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		String unlocalizedName = stack.getUnlocalizedName();
		String tooltipKey = unlocalizedName + ".tooltip";
		if (Translator.canTranslateToLocal(tooltipKey)) {
			String tooltipInfo = Translator.translateToLocal(tooltipKey);
			Minecraft minecraft = Minecraft.getMinecraft();
			List<String> tooltipInfoWrapped = minecraft.fontRenderer.listFormattedStringToWidth(tooltipInfo, 150);
			tooltip.addAll(tooltipInfoWrapped);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void addShiftInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(TextFormatting.ITALIC .toString() + '<' + Translator.translateToLocal("for.gui.tooltip.tmi") + '>');
	}

	@SideOnly(Side.CLIENT)
	public static List<String> getInformation(ItemStack stack){
		Minecraft minecraft = Minecraft.getMinecraft();
		boolean advancedTooltips = minecraft.gameSettings.advancedItemTooltips;
		return getInformation(stack, minecraft.thePlayer, advancedTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
	}

	@SideOnly(Side.CLIENT)
	public static List<String> getInformation(ItemStack stack, EntityPlayer player, ITooltipFlag flag){
		if (stack == null) {
			return Collections.emptyList();
		}
		List<String> tooltip = stack.getTooltip(player, flag);
		for (int i = 0; i < tooltip.size(); ++i) {
			if (i == 0) {
				tooltip.set(i, stack.getRarity().rarityColor + tooltip.get(i));
			} else {
				tooltip.set(i, TextFormatting.GRAY + tooltip.get(i));
			}
		}
		return tooltip;
	}
}