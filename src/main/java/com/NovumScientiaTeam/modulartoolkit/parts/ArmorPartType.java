package com.NovumScientiaTeam.modulartoolkit.parts;

import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.PartType;
import net.minecraft.inventory.EquipmentSlotType;

public abstract class ArmorPartType extends PartType {
    public ArmorPartType(String name) {
        super(name);
    }

    public abstract int getExtraDurability(Material mat, EquipmentSlotType slotType);

    public abstract double getDurabilityModifier(Material mat, EquipmentSlotType slotType);

    public abstract int getDefence(Material mat, EquipmentSlotType slotType);
}
