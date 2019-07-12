package com.NovumScientiaTeam.modulartoolkit;

import com.EmosewaPixel.pixellib.materialsystem.MaterialRegistry;
import com.EmosewaPixel.pixellib.materialsystem.types.ItemType;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;

public class ObjTypeRegistry {
    public static ObjectType TOOL_ROD, AXE_HEAD, HOE_HEAD, PICKAXE_HEAD, SWORD_BLADE, BINDING, SHOVEL_HEAD, SWORD_GUARD, WRENCH_HEAD, FRAGMENT;

    public static final String HEAD = "tool_head";
    public static final String HANDLE = "tool_handle";
    public static final String EXTRA = "tool_extra";
    public static final String WEAPON_PART = "weapon_part";

    static {
        TOOL_ROD = new ItemType("tool_rod", m -> m.getItemTier() != null).setBucketVolume(144).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, HANDLE).build();
        AXE_HEAD = new ItemType("axe_head", m -> m.getItemTier() != null).setBucketVolume(432).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, HEAD).build();
        HOE_HEAD = new ItemType("hoe_head", m -> m.getItemTier() != null).setBucketVolume(288).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, HEAD).build();
        PICKAXE_HEAD = new ItemType("pickaxe_head", m -> m.getItemTier() != null).setBucketVolume(432).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, HEAD).build();
        SHOVEL_HEAD = new ItemType("shovel_head", m -> m.getItemTier() != null).setBucketVolume(144).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, HEAD).build();
        SWORD_BLADE = new ItemType("sword_blade", m -> m.getItemTier() != null).setBucketVolume(144).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, HEAD, WEAPON_PART).build();
        BINDING = new ItemType("binding", m -> m.getItemTier() != null).setBucketVolume(72).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, EXTRA).build();
        SWORD_GUARD = new ItemType("sword_guard", m -> m.getItemTier() != null).setBucketVolume(72).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, EXTRA, WEAPON_PART).build();
        WRENCH_HEAD = new ItemType("wrench_head", m -> m.getItemTier() != null).setBucketVolume(144).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, HEAD).build();
        FRAGMENT = new ItemType("fragment", m -> m.getItemTier() != null).setBucketVolume(72).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
    }
}
