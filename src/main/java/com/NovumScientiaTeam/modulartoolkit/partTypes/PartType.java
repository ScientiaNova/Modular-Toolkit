package com.NovumScientiaTeam.modulartoolkit.partTypes;

import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public abstract class PartType {
    public abstract int getExtraDurability(Material mat);

    public abstract double getDurabilityModifier(Material mat);

    public abstract double getLevelCapMultiplier(Material mat);

    public abstract String getName();

    public abstract void addTooltip(Item item, List<ITextComponent> tooltip);
}
