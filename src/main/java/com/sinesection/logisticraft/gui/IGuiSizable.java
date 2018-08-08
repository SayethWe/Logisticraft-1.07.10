package com.sinesection.logisticraft.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public interface IGuiSizable {

	int getGuiLeft();

	int getGuiTop();

	int getSizeX();

	int getSizeY();

	Minecraft getMC();
}