package com.NovumScientiaTeam.modulartoolkit.parts.partTypes;

import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.modifications.PartModification;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PartType {
    private String name;
    protected List<PartModification> mods = new ArrayList<>();

    public PartType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PartType addMods(PartModification... mods) {
        this.mods.addAll(Arrays.asList(mods));
        return this;
    }

    public abstract int getExtraDurability(Material mat);

    public abstract double getDurabilityModifier(Material mat);

    public abstract double getLevelCapMultiplier(Material mat);

    public abstract void addTooltip(Item item, List<ITextComponent> tooltip);
}
