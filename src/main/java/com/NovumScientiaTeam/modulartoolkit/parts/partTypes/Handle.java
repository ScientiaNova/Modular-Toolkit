package com.NovumScientiaTeam.modulartoolkit.parts.partTypes;

import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.abilities.Abilities;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.modifications.PartModification;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;
import java.util.List;

public class Handle extends PartType {
    public Handle() {
        super("handle");
    }

    @Override
    public int getExtraDurability(Material mat) {
        IItemTier tier = mat.getItemTier();
        return (int) ((tier.getMaxUses() - (tier.getEfficiency() / (tier.getEnchantability()) * tier.getMaxUses() * 2)) / 2 * mods.stream().mapToDouble(PartModification::getExtraDurabilityMultiplier).reduce(1, (d1, d2) -> d1 * d2));
    }

    @Override
    public double getDurabilityModifier(Material mat) {
        IItemTier tier = mat.getItemTier();
        double n = (double) tier.getMaxUses() / (tier.getHarvestLevel() + 1) / (2 + tier.getEnchantability() * 0.5 + tier.getHarvestLevel() * 1.5);
        double gradient = tier.getEnchantability() / tier.getEfficiency() * 2;
        return (((-n + 1) * gradient) / (n + 1 + gradient) + gradient) / gradient * 1.35;
    }

    @Override
    public double getLevelCapMultiplier(Material mat) {
        return 1;
    }

    @Override
    public void addTooltip(Item item, List<ITextComponent> tooltip) {
        DecimalFormat format = new DecimalFormat("#.##");
        Material mat = ((IMaterialItem) item).getMaterial();
        tooltip.add(new TranslationTextComponent("tool.stat.durability", Integer.toString(getExtraDurability(mat))));
        tooltip.add(new TranslationTextComponent("tool.stat.durability_multiplier", format.format(getDurabilityModifier(mat))));
        AbstractAbility ability = Abilities.getFor(mat, this);
        if (ability != null)
            tooltip.add(ability.getTranslationKey(new ItemStack(item)));
    }
}