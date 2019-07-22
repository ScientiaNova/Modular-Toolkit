package com.NovumScientiaTeam.modulartoolkit.parts;

import com.EmosewaPixel.pixellib.materialsystem.MaterialRegistry;
import com.EmosewaPixel.pixellib.materialsystem.types.ItemType;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.*;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.modifications.ToughMod;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.common.ToolType;

public class ObjTypeRegistry {
    public static ObjectType TOOL_ROD, AXE_HEAD, HOE_HEAD, PICKAXE_HEAD, SWORD_BLADE, BINDING, SHOVEL_HEAD, SWORD_GUARD, WRENCH_HEAD, FRAGMENT, TOUGH_TOOL_ROD, TOUGH_BINDING, PLATING, SHIELD_FRAME;

    public static final String WEAPON_PART = "weapon_part";

    public static final ToolType WRENCH = ToolType.get("wrench");

    static {
        TOOL_ROD = new ItemType("tool_rod", m -> m.getItemTier() != null).setBucketVolume(144).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        AXE_HEAD = new ItemType("axe_head", m -> m.getItemTier() != null).setBucketVolume(432).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        HOE_HEAD = new ItemType("hoe_head", m -> m.getItemTier() != null).setBucketVolume(288).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        PICKAXE_HEAD = new ItemType("pickaxe_head", m -> m.getItemTier() != null).setBucketVolume(432).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        SHOVEL_HEAD = new ItemType("shovel_head", m -> m.getItemTier() != null).setBucketVolume(144).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        SWORD_BLADE = new ItemType("sword_blade", m -> m.getItemTier() != null).setBucketVolume(144).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, WEAPON_PART).build();
        BINDING = new ItemType("binding", m -> m.getItemTier() != null).setBucketVolume(72).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        SWORD_GUARD = new ItemType("sword_guard", m -> m.getItemTier() != null).setBucketVolume(72).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE, WEAPON_PART).build();
        WRENCH_HEAD = new ItemType("wrench_head", m -> m.getItemTier() != null).setBucketVolume(144).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        FRAGMENT = new ItemType("fragment", m -> m.getItemTier() != null || m.getArmorMaterial() != null).setBucketVolume(72).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        TOUGH_TOOL_ROD = new ItemType("tough_tool_rod", m -> m.getItemTier() != null).setBucketVolume(432).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        TOUGH_BINDING = new ItemType("tough_binding", m -> m.getItemTier() != null).setBucketVolume(216).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        PLATING = new ItemType("plating", m -> m.getArmorMaterial() != null).setBucketVolume(216).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        SHIELD_FRAME = new ItemType("shield_frame", m -> m.getArmorMaterial() != null).setBucketVolume(288).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();

        PartTypeMap.bindToPartType(TOOL_ROD, new Handle());
        PartTypeMap.bindToPartType(AXE_HEAD, new Head(ToolType.AXE));
        PartTypeMap.bindToPartType(HOE_HEAD, new Head());
        PartTypeMap.bindToPartType(PICKAXE_HEAD, new Head(ToolType.PICKAXE));
        PartTypeMap.bindToPartType(SHOVEL_HEAD, new Head(ToolType.SHOVEL));
        PartTypeMap.bindToPartType(SWORD_BLADE, new Head());
        PartTypeMap.bindToPartType(BINDING, new Extra());
        PartTypeMap.bindToPartType(SWORD_GUARD, new Extra());
        PartTypeMap.bindToPartType(WRENCH_HEAD, new Head(WRENCH));
        PartTypeMap.bindToPartType(TOUGH_TOOL_ROD, new Handle().addMods(new ToughMod()));
        PartTypeMap.bindToPartType(TOUGH_BINDING, new Extra().addMods(new ToughMod()));
        PartTypeMap.bindToPartType(PLATING, new Plating());
        PartTypeMap.bindToPartType(SHIELD_FRAME, new Frame(EquipmentSlotType.LEGS));
    }
}