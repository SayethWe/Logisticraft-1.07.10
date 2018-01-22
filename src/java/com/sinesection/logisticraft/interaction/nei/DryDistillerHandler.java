package com.sinesection.logisticraft.interaction.nei;

import com.sinesection.logisticraft.registrars.ModBlocks;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class DryDistillerHandler extends TemplateRecipeHandler {

	public class DistillerRecipe extends CachedRecipe {

		@Override
		public PositionedStack getResult() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	@Override
	public String getRecipeName() {
		return ModBlocks.dryDistillerIdle.getLocalizedName();
	}

	@Override
	public String getGuiTexture() {
		return "/logisticraft/textures/gui/guiDistiller.png";
	}

}
