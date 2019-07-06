package com.NovumScientiaTeam.modulartoolkit.partTypes;

import com.EmosewaPixel.pixellib.materialSystem.materials.Material;

public class Extra extends PartType {
    @Override
    public int getExtraDurability(Material mat) {
        return mat.getItemTier().getMaxUses() / 5;
    }

    @Override
    public double getDurabilityModifier(Material mat) {
        return 1;
    }

    @Override
    public double getLevelCapMultiplier(Material mat) {
        return mat.getItemTier().getEnchantability() / 10d;
    }

    @Override
    public String getName() {
        return "extra";
    }
}
