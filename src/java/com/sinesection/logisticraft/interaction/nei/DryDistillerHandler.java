package com.sinesection.logisticraft.interaction.nei;

import java.util.List;

import com.sinesection.logisticraft.registrars.ModBlocks;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.item.ItemStack;

public class DryDistillerHandler extends TemplateRecipeHandler {

	public class DistillerRecipe extends CachedRecipe {
		
		private final ItemStack input;
		
		public DistillerRecipe(ItemStack in) {
			this.input = in;
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PositionedStack getIngredient() {
			// TODO Auto-generated method stub
			return new PositionedStack(input, 25, 50);
		}

		@Override
		public PositionedStack getResult() {
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
