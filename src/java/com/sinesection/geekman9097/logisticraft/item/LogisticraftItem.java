package com.sinesection.geekman9097.logisticraft.item;

import com.sinesection.geekman9097.logisticraft.Main;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class LogisticraftItem extends Item {
	
	String registryName;

	/**
	 * for when you just don't see the need to have a ton of different names.
	 * Also automagically places it in the "Items" tab.
	 * @param universalName the only name you ever need
	 */
	public LogisticraftItem(String universalName) {
		//register a simple Item
		this(universalName,universalName,universalName,Main.tabLogisticraftItems);
	}
	
	public LogisticraftItem(String unlocalizedName, String textureName, String registryName, CreativeTabs creativeTab) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(Main.MODID + ":" + textureName);
		this.registryName = registryName;
		this.setCreativeTab(creativeTab);
	}
	
	public String getRegistryName() {
		return registryName;
	}
	
}
