package com.sinesection.logisticraft;

import com.sinesection.logisticraft.proxy.CommonProxy;
import com.sinesection.logisticraft.registrars.ModBlocks;
import com.sinesection.logisticraft.registrars.ModItems;
import com.sinesection.utils.Utils;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(modid=Main.MODID, name = Main.MOD_NAME, version = Main.VERSION)
public class Main {
	public final static String MODID = "logisticraft";
	public final static String MOD_NAME = "Logisticraft";
	/**
	 * The version number, seperated as:
	 * Release Number (Full Codebase overhaul, large rewrite, etc)
	 * Update Number (Update to new MC Version)
	 * Feature Number (new features, etc)
	 */
	public final static String VERSION = "0.0.0";
	

	@Instance
	public static Main instance = new Main();
	
	@SidedProxy(clientSide="com.sinesection.logisticraft.proxy.ClientProxy", serverSide="com.sinesection.logisticraft.proxy.ServerProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		Utils.createLogger(MOD_NAME);
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

	public static final CreativeTabs tabLogisticraftItems = new CreativeTabs("tabLogisticraftItems") {
		@Override
		public ItemStack getIconItemStack() {
			return ModItems.creativeTabIconItemStack;
		}

		@Override
		public Item getTabIconItem() {
			return null;
		}
	};
	
	public static final CreativeTabs tabLogisticraftBlocks = new CreativeTabs("tabLogisticraftBlocks") {
		@Override
		public Item getTabIconItem() {
			return ModBlocks.creativeTabIconItem;
		}
	};
	
}
