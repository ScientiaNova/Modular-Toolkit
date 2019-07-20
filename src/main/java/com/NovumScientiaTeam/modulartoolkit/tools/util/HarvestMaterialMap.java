package com.NovumScientiaTeam.modulartoolkit.tools.util;

import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

import java.util.HashMap;
import java.util.Map;

public class HarvestMaterialMap {
    public static final Map<Material, ToolType> MAP = new HashMap<>();

    public static void add(ToolType type, Material... blockMaterials) {
        for (Material mat : blockMaterials)
            MAP.put(mat, type);
    }

    public static boolean contains(Material blockMat) {
        return MAP.containsKey(blockMat);
    }

    public static ToolType get(Material blockMat) {
        return MAP.get(blockMat);
    }

    public static void init() {
        add(ToolType.AXE, Material.WOOD, Material.PLANTS, Material.TALL_PLANTS, Material.BAMBOO);
        add(ToolType.PICKAXE, Material.IRON, Material.ANVIL, Material.ROCK);
    }
}