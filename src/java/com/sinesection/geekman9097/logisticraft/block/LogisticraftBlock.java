package com.sinesection.geekman9097.logisticraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class LogisticraftBlock extends Block {
	
	private final String registryName;
	
	public LogisticraftBlock(String universalName, Material mat) {
		this(universalName, universalName, universalName, mat);
	}

	protected LogisticraftBlock(String name, String textureName, String registryName, Material mat) {
		super(mat);
		this.setBlockName(name);
		this.setBlockTextureName(textureName);
		this.registryName = registryName;
	}
	
	public String getRegistryName() {
		return registryName;
	}

}
