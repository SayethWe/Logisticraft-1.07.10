package com.sinesection.logisticraft.item.renderers;

import com.sinesection.logisticraft.item.ItemLogisticraftBucket;
import com.sinesection.utils.Log;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class LogisticraftItemBucketRenderer implements IItemRenderer {

	static final boolean testflagIsFull3D = false; // if true - rotate the icon
													// in EQUIPPED 3rd person
													// view - eg sword. If false
													// - don't rotate, eg
													// pumpkin pie
	static boolean wrongRendererMsgWritten = false;

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case ENTITY:
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		switch (type) {
		case ENTITY:
			return (helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.ENTITY_ROTATION);
		case EQUIPPED:
			return false;
		case EQUIPPED_FIRST_PERSON:
			return false;
		case INVENTORY:
			return false;
		default:
			return false;
		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == ItemRenderType.INVENTORY) {
			drawAs2D(type, item);
		} else {
			drawAsSlice(type, item);
		}

		return;
	}

	private void drawAs2D(ItemRenderType type, ItemStack item) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		IIcon icon1 = item.getItem().getIconFromDamage(0);

		double minU1 = (double) icon1.getMinU();
		double minV1 = (double) icon1.getMinV();
		double maxU1 = (double) icon1.getMaxU();
		double maxV1 = (double) icon1.getMaxV();

		tessellator.addVertexWithUV(16.0, 16.0, 0.0, maxU1, maxV1);
		tessellator.addVertexWithUV(16.0, 0.0, 0.0, maxU1, minV1);
		tessellator.addVertexWithUV(0.0, 0.0, 0.0, minU1, minV1);
		tessellator.addVertexWithUV(0.0, 16.0, 0.0, minU1, maxV1);
		tessellator.draw();
	}

	private void drawAsSlice(ItemRenderType type, ItemStack item) {
		ItemLogisticraftBucket bucketItem;
		if (item.getItem() instanceof ItemLogisticraftBucket) {
			bucketItem = (ItemLogisticraftBucket) item.getItem();
		} else {
			if (!wrongRendererMsgWritten) {
				wrongRendererMsgWritten = true;
				Log.info(this.getClass().getName() + ".drawAsSlice() called with wrong Item:" + item.getDisplayName());
			}
			return;
		}

		final float THICKNESS = 0.0625F;

		Tessellator tessellator = Tessellator.instance;
		// GL11.glTranslatef(-0.5F, 0.0F, 0.0F);

		IIcon icon = bucketItem.getIcon(item, 0);
		IIcon fluidIcon = bucketItem.getFluid().getStillIcon();

		double minU1 = (double) fluidIcon.getMinU();
		double minV1 = (double) fluidIcon.getMinV();
		double maxU1 = (double) fluidIcon.getMaxU();
		double maxV1 = (double) fluidIcon.getMaxV();

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(1.0, 1.0, 0.0, minU1, maxV1);
		tessellator.addVertexWithUV(1.0, 0.0, 0.0, maxU1, maxV1);
		tessellator.addVertexWithUV(0.0, 0.0, 0.0, maxU1, minV1);
		tessellator.addVertexWithUV(0.0, 1.0, 0.0, minU1, minV1);
		tessellator.draw();

		ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMaxV(), icon.getMinU(), icon.getMinV(), 1, 1, THICKNESS);
	}

}
