package com.NovumScientiaTeam.modulartoolkit.partTypes;

import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import net.minecraftforge.common.ToolType;

import java.util.Optional;

public class Head extends PartType {
    private ToolType toolType;

    public Head(ToolType toolType) {
        this.toolType = toolType;
    }

    public Head() {
        this(null);
    }

    @Override
    public int getExtraDurability(Material mat) {
        return (int) (mat.getItemTier().getMaxUses() * 0.8);
    }

    @Override
    public double getDurabilityModifier(Material mat) {
        return 1;
    }

    @Override
    public double getLevelCapMultiplier(Material mat) {
        return mat.getItemTier().getEnchantability() * mat.getItemTier().getEfficiency() / 10;
    }

    public Optional<ToolType> getToolType() {
        return Optional.ofNullable(toolType);
    }

    @Override
    public String getName() {
        return "head";
    }
}
