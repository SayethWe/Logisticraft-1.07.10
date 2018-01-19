package com.sinesection.logisticraft.fluid;

import java.util.HashMap;
import java.util.Map;

import com.sinesection.logisticraft.item.ItemLogisticraftBucket;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class LogisticraftFluidHandler {
	
	public static LogisticraftFluidHandler INSTANCE = new LogisticraftFluidHandler();
    private static Map<Block, Item> buckets = new HashMap<Block, Item>();

	public LogisticraftFluidHandler() {
    }
	
	public static boolean registerLogisticraftFluid(LogisticraftBlockFluid block, ItemLogisticraftBucket item) {
		if(!buckets.containsKey(block))
			return false;

		buckets.put((Block) block, item);
		return true;
	}

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {
        ItemStack result = fillCustomBucket(event.world, event.target);
        System.out.println("saucey: " + result.getDisplayName());
        if (result == null)
            return;

        event.result = result;
        event.setResult(Event.Result.ALLOW);
    }

    private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) {
        Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);

        Item bucket = buckets.get(block);
        if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
            world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
            return new ItemStack(bucket);
        } else
            return null;

    }

}
