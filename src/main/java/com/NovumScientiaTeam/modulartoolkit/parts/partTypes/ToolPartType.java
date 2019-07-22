package com.NovumScientiaTeam.modulartoolkit.parts.partTypes;

import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.modifications.PartModification;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ToolPartType extends PartType {
    public ToolPartType(String name) {
        super(name);
    }

    public abstract int getExtraDurability(Material mat);

    public abstract double getDurabilityModifier(Material mat);
}
