package com.sinesection.logisticraft.interaction.nei;

import com.sinesection.logisticraft.Main;
import com.sinesection.utils.LogisticraftUtils;

import codechicken.nei.api.IConfigureNEI;

public class LogisticraftNEIConfig implements IConfigureNEI{
	
	private static final DryDistillerHandler dryDistillerHandler = new DryDistillerHandler();
	private static final MixerHandler mixerHandler = new MixerHandler();
	private static final FractionationHandler fractionHandler = new FractionationHandler();
	
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
