package com.sinesection.logisticraft.api.core;

import net.minecraft.world.World;

/**
 * Interface for things, that have a location.
 * Must not be named "getWorld" and "getPos" to avoid 
 * SpecialSource issue https://github.com/md-5/SpecialSource/issues/12
 */
public interface ILocatable {
	int getXCoord();
	int getYCoord();
	int getZCoord();
	
	World getWorldObj();
}
