package com.sinesection.logisticraft.interaction.nei;

import com.sinesection.logisticraft.Main;
import com.sinesection.utils.LogisticraftUtils;

import codechicken.nei.api.IConfigureNEI;

public class LogisticraftNEIConfig implements IConfigureNEI{
	
	private static final dryDistillerHandler = new DryDistillerHandler();
	private static final mixerHandler = new mixerHandler();
	
	@Override
	public void loadConfig() {
		LogisticraftUtils.getLogger().info("Loading NEI Compat");
	}

	@Override
	public String getName() {
		return Main.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return Main.VERSION;
	}

}
