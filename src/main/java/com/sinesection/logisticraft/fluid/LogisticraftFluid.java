package com.sinesection.logisticraft.fluid;

import com.sinesection.logisticraft.Constants;
import com.sinesection.logisticraft.registrars.ModMaterials;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class LogisticraftFluid extends Fluid {

	protected LogisticraftBlockFluid fluidBlock;

	protected String stillTextureName, flowingTextureName;
	protected int color = 0xFFFFFF, flammability = 0;
	protected Material material = Material.water;
	protected boolean affectTexture = false, toxic = false;

	public LogisticraftFluid(String fluidName) {
		super(fluidName);
		setTextureNames(Constants.MOD_ID + ":" + fluidName + "_still", Constants.MOD_ID + ":" + fluidName + "_flowing");
	}

	public LogisticraftFluid(String fluidName, int mapColor) {
		this(fluidName);
		setColor(mapColor);
	}

	@Override
	public int getColor() {
		return color;
	}

	public LogisticraftFluid setFuel() {
		this.setMaterial(ModMaterials.fuel);
		flammability = 300;
		toxic = true;
		return this;
	}

	public LogisticraftFluid setToxic() {
		toxic = true;
		return this;
	}

	public LogisticraftFluid setColor(int color, boolean affectTexture) {
		this.color = color;
		this.affectTexture = affectTexture;
		return this;
	}

	public LogisticraftFluid setColor(int color) {
		return setColor(color, false);
	}

	public LogisticraftFluid setFlammability(int flammability) {
		if (flammability > 300)
			flammability = 300;
		if (flammability < 0)
			flammability = 0;
		this.flammability = flammability;
		return this;
	}

	public LogisticraftFluid setMaterial(Material material) {
		this.material = material;
		return this;
	}

	public LogisticraftFluid setStillTextureName(String textureName) {
		this.stillTextureName = textureName;
		return this;
	}

	public LogisticraftFluid setFlowingTextureName(String textureName) {
		this.flowingTextureName = textureName;
		return this;
	}

	public LogisticraftFluid setTextureNames(String stillTextureName, String flowingTextureName) {
		setStillTextureName(stillTextureName);
		setFlowingTextureName(flowingTextureName);
		return this;
	}

	/**
	 * Careful using this, <b>will</b> cause crashes if the given fluid does not
	 * have icons.
	 * 
	 * @param fluid
	 *            Fluid to copy icons from.
	 * @return This {@link LogisticraftFluid}.
	 */
	@Deprecated
	public LogisticraftFluid setTextureNamesFromFluid(Fluid fluid) {
		setStillTextureName(fluid.getStillIcon().getIconName());
		setFlowingTextureName(fluid.getFlowingIcon().getIconName());
		return this;
	}

	public String getStillTextureName() {
		return this.stillTextureName;
	}

	public String getFlowingTextureName() {
		return this.flowingTextureName;
	}

	public Material getMaterial() {
		return material;
	}

	public boolean shouldColorAffectTexture() {
		return affectTexture;
	}

	public int getFlammability() {
		return flammability;
	}

	public boolean isToxic() {
		return toxic;
	}

	/**
	 * Use {@link #setColor()} instead.
	 * 
	 * @param mapColor
	 */
	@Deprecated
	public void setMapColor(int mapColor) {
		setColor(mapColor);
	}

}
