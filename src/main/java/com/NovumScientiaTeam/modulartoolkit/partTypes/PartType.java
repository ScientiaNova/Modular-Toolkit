package com.NovumScientiaTeam.modulartoolkit.partTypes;

import com.EmosewaPixel.pixellib.materialsystem.materials.Material;

public abstract class PartType {
    public abstract int getExtraDurability(Material mat);

    public abstract double getDurabilityModifier(Material mat);

    public abstract double getLevelCapMultiplier(Material mat);

    public abstract String getName();
}
