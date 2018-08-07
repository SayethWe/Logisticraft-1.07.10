package com.sinesection.logisticraft.network;

import com.sinesection.logisticraft.block.tileentity.TileEntityDryDistiller;
import com.sinesection.logisticraft.block.tileentity.TileEntityFractionator;
import com.sinesection.logisticraft.block.tileentity.TileEntityMixer;
import com.sinesection.logisticraft.container.ContainerDryDistiller;
import com.sinesection.logisticraft.container.ContainerFractionator;
import com.sinesection.logisticraft.gui.GuiDryDistiller;
import com.sinesection.logisticraft.gui.GuiFractionator;
import com.sinesection.logisticraft.gui.GuiMixer;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LogisticraftGuiHandler implements IGuiHandler {

	public static final int guiIdDryDistiller = 0;
	public static final int guiIdFractionator = 1;
	public static final int guiIdMixer = 2;
	public static final int guiIdHandbook = 3;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tEntity = world.getTileEntity(x, y, z);
		switch (ID) {
		case guiIdDryDistiller:
			if (tEntity != null)
				if (tEntity instanceof TileEntityDryDistiller)
					return new ContainerDryDistiller(player.inventory, (TileEntityDryDistiller) tEntity);
		case guiIdFractionator:
			if (tEntity != null)
				if (tEntity instanceof TileEntityFractionator)
					return new ContainerFractionator(player.inventory, (TileEntityFractionator) tEntity);
		case guiIdMixer:
			if (tEntity != null)
				if (tEntity instanceof TileEntityMixer)
					return new ContainerMixer(player.inventory, (TileEntityMixer) tEntity);
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tEntity = world.getTileEntity(x, y, z);
		switch (ID) {
		case guiIdDryDistiller:
			if (tEntity != null)
				if (tEntity instanceof TileEntityDryDistiller)
					return new GuiDryDistiller(player.inventory, (TileEntityDryDistiller) tEntity);
		case guiIdFractionator:
			if (tEntity != null)
				if (tEntity instanceof TileEntityFractionator)
					return new GuiFractionator(player.inventory, (TileEntityFractionator) tEntity);
		case guiIdMixer:
			if (tEntity != null)
				if (tEntity instanceof TileEntityMixer)
					return new GuiMixer(player.inventory, (TileEntityMixer) tEntity);
		default:
			return null;
		}
	}
}