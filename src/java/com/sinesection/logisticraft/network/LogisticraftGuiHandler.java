package com.sinesection.logisticraft.network;

import com.sinesection.logisticraft.container.ContainerDryDistiller;
import com.sinesection.logisticraft.gui.GuiDryDistiller;
import com.sinesection.logisticraft.tileentity.TileEntityDryDistiller;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LogisticraftGuiHandler implements IGuiHandler {
	
	public static final int guiIdDryDistiller = 0;
	public static final int guiIdFractionator = 1;
	public static final int guiIdHandbook = 2;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tEntity = world.getTileEntity(x, y, z);
		switch(ID) {
		case guiIdDryDistiller:
			if(tEntity != null)
				if(tEntity instanceof TileEntityDryDistiller)
					return new ContainerDryDistiller(player.inventory, (TileEntityDryDistiller) tEntity);
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tEntity = world.getTileEntity(x, y, z);
		switch(ID) {
		case guiIdDryDistiller:
			if(tEntity != null)
				if(tEntity instanceof TileEntityDryDistiller)
					return new GuiDryDistiller(player.inventory, (TileEntityDryDistiller) tEntity);
		default:
			return null;
		}
	}
}