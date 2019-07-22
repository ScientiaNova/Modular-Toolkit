package com.NovumScientiaTeam.modulartoolkit.parts.partTypes;

import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.abilities.Abilities;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.parts.ArmorPartType;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.modifications.PartModification;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Plating extends ArmorPartType {
    public Plating() {
        super("plating");
    }

    @Override
    public int getExtraDurability(Material mat, EquipmentSlotType slotType) {
        return (int) (mat.getArmorMaterial().getDurability(slotType) * 0.5 * mods.stream().mapToDouble(PartModification::getExtraDurabilityMultiplier).reduce(1, (d1, d2) -> d1 * d2));
    }

    @Override
    public double getDurabilityModifier(Material mat, EquipmentSlotType slotType) {
        return 1;
    }

    @Override
    public double getLevelCapMultiplier(Material mat) {
        return mat.getArmorMaterial().getEnchantability();
    }

    @Override
    public void addTooltip(Item item, List<ITextComponent> tooltip) {
        DecimalFormat format = new DecimalFormat("#.##");
        Material mat = ((IMaterialItem) item).getMaterial();
        Arrays.stream(EquipmentSlotType.values()).filter(t -> t.getSlotType() == EquipmentSlotType.Group.ARMOR).forEach(slotType ->
            tooltip.add(new TranslationTextComponent("tool.stat.durability_" + slotType.getName().toLowerCase(), Integer.toString(getExtraDurability(mat, slotType)))));
            tooltip.add(new TranslationTextComponent("tool.stat.level_cap_multiplier", format.format(getLevelCapMultiplier(mat))));
        AbstractAbility ability = Abilities.getFor(mat, this);
        if (ability != null)
            tooltip.add(ability.getTranslationKey(new ItemStack(item)));
    }
}