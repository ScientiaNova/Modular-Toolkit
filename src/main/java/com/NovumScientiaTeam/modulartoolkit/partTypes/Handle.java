package com.NovumScientiaTeam.modulartoolkit.partTypes;

import com.EmosewaPixel.pixellib.materialSystem.materials.Material;
import net.minecraft.item.IItemTier;

public class Handle extends PartType {
    @Override
    public int getExtraDurability(Material mat) {
        IItemTier tier = mat.getItemTier();
        return (int) ((tier.getMaxUses() - (tier.getEfficiency() / (tier.getEnchantability()) * tier.getMaxUses() * 2)) / 2);
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
    public String getName() {
        return "handle";
    }
}