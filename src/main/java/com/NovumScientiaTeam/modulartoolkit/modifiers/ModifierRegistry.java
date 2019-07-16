package com.NovumScientiaTeam.modulartoolkit.modifiers;

import net.minecraft.item.Items;

public class ModifierRegistry {
    public static void register() {
        Modifiers.addModifier(Items.QUARTZ, new SharpnessMod());
    }
}
