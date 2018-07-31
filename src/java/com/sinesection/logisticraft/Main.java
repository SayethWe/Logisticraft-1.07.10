package com.sinesection.logisticraft;

import com.sinesection.logisticraft.proxy.CommonProxy;
import com.sinesection.logisticraft.registrars.ModBlocks;
import com.sinesection.logisticraft.registrars.ModItems;
import com.sinesection.utils.LogisticraftUtils;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(modid=Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class Main {
	

	@Instance
	public static Main instance = new Main();
	
	@SidedProxy(clientSide="com.sinesection.logisticraft.proxy.ClientProxy", serverSide="com.sinesection.logisticraft.proxy.ServerProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		LogisticraftUtils.createLogger(Constants.MOD_NAME);
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
			return new ItemStack(ModItems.roadWheel, 1, 1);
		};
		
		@Override
		public Item getTabIconItem() {
			return getIconItemStack().getItem();
		}
	};
	
	public static final CreativeTabs tabLogisticraftBlocks = new CreativeTabs("tabLogisticraftBlocks") {
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(ModBlocks.dryDistillerIdle);
		}
	};
	
}
