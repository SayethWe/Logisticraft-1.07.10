package com.sinesection.logisticraft.fluid;

public class FakeTankUpdateHandler implements ITankUpdateHandler {

	public static final FakeTankUpdateHandler instance = new FakeTankUpdateHandler();

	private FakeTankUpdateHandler() {

	}

	@Override
	public void updateTankLevels(StandardTank tank) {

	}
}
