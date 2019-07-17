package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.EmosewaPixel.pixellib.materialsystem.MaterialRegistry;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Extra;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Handle;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;

public class AbilityRegistry {
    public static final SoftAbility SOFT = new SoftAbility();

    public static void register() {
        Abilities.addAbility(MaterialRegistry.GOLD, new Head(), SOFT);
        Abilities.addAbility(MaterialRegistry.GOLD, new Extra(), SOFT);
        Abilities.addAbility(MaterialRegistry.GOLD, new Handle(), SOFT);
    }
}