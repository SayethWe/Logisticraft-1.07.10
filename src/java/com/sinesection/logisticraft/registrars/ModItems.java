package com.sinesection.logisticraft.registrars;

import java.util.HashSet;
import java.util.Set;

import com.sinesection.logisticraft.item.ItemHandbook;
import com.sinesection.logisticraft.item.LogisticraftItem;

import cpw.mods.fml.common.registry.GameRegistry;

public final class ModItems {
	
	public static LogisticraftItem creativeTabIconItem;
	
	public static final LogisticraftItem roadWheel = new LogisticraftItem("wheelWood");
	public static final LogisticraftItem refinedRubber = new LogisticraftItem("rubber");
	public static final LogisticraftItem shippingOrder = new LogisticraftItem("shippingOrder");
	public static final LogisticraftItem handbook = new ItemHandbook();
	public static final LogisticraftItem sulfur = new LogisticraftItem("sulfur");
	public static final LogisticraftItem resin = new LogisticraftItem("resin");
	public static final LogisticraftItem tar = new LogisticraftItem("tar");

	public static final Set<LogisticraftItem> items = new HashSet<>();
	
	public static final void registerItems() {
		for(LogisticraftItem item : items) {
			GameRegistry.registerItem(item, item.getRegistryName());
		}
	}
	
	public static void loadItems() {
		creativeTabIconItem = roadWheel;
		loadItem(roadWheel);
		loadItem(refinedRubber);
		loadItem(shippingOrder);
		loadItem(handbook);
		loadItem(sulfur);
		loadItem(resin);
		loadItem(tar);
	}
	
	private static void loadItem(LogisticraftItem i) {
		items.add(i);
	}
}
