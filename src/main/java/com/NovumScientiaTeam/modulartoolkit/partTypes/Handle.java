package com.NovumScientiaTeam.modulartoolkit.partTypes;

import com.EmosewaPixel.pixellib.materialSystem.materials.Material;
import net.minecraft.item.IItemTier;

public class Handle extends PartType {
    @Override
    public int getExtraDurability(Material mat) {
        IItemTier tier = mat.getItemTier();
        return (int) ((tier.getMaxUses() - (tier.getEfficiency() / tier.getEnchantability() * tier.getMaxUses() * 2)) / 2);
    }

    @Override
    public double getDurabilityModifier(Material mat) {
        IItemTier tier = mat.getItemTier();
        double value = tier.getMaxUses() / tier.getEfficiency() / tier.getEnchantability();
        for (int i = 0; i <= tier.getHarvestLevel(); i++)
            value = Math.sqrt(value);
        return (1 + 1 / tier.getEfficiency()) / value;
    }

    @Override
    public double getLevelCapMultiplier(Material mat) {
        return 1;
    }

    @Override
    public String getName() {
        return "handle";
    }
}