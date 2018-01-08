package com.sinesection.geekman9097.logisticraft.item;

import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;

public final class ModItems {
	
	public static LogisticraftItem creativeTabIconItem;
	
	public static LogisticraftItem roadWheel = new LogisticraftItem("wheelWood");
	public static LogisticraftItem refinedRubber = new LogisticraftItem("rubber");
	public static LogisticraftItem shippingOrder = new LogisticraftItem("shippingOrder");
	public static LogisticraftItem handbook = new ItemHandbook();

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
	}
	
	private static void loadItem(LogisticraftItem i) {
		items.add(i);
	}
}
