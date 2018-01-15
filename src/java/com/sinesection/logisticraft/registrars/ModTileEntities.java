package com.sinesection.logisticraft.registrars;

import java.util.HashMap;
import java.util.Map;

import com.sinesection.logisticraft.block.tileentity.LogisticraftTileEntity;
import com.sinesection.logisticraft.block.tileentity.TileEntityDryDistiller;
import com.sinesection.logisticraft.block.tileentity.TileEntityFractionator;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileEntities {

    private static final Map<String, Class<? extends LogisticraftTileEntity>> tileEnts = new HashMap<>();
    
    public static void registerTileEntities() {
        for(String s : tileEnts.keySet()) {
            GameRegistry.registerTileEntity(tileEnts.get(s), s);
        }
    }
    
    public static void loadTileEntities() {
    	tileEnts.put("dryDistiller", TileEntityDryDistiller.class);
    	tileEnts.put("fractionator", TileEntityFractionator.class);
    }
}