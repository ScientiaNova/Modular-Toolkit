package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.EmosewaPixel.pixellib.materialsystem.MaterialRegistry;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Extra;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Handle;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;

public class AbilityRegistry {
    public static final SoftAbility SOFT = new SoftAbility();
    public static final CheapAbility CHEAP = new CheapAbility();
    public static final RustableAbility RUSTABLE = new RustableAbility();
    public static final PhotosynthesisAbility PHOTOSYNTHESIS = new PhotosynthesisAbility();
    public static final MagneticAbility MAGNETIC = new MagneticAbility();

    public static void register() {
        Abilities.addAbility(MaterialRegistry.GOLD, new Head(), SOFT);
        Abilities.addAbility(MaterialRegistry.GOLD, new Extra(), SOFT);
        Abilities.addAbility(MaterialRegistry.GOLD, new Handle(), SOFT);
        Abilities.addAbility(MaterialRegistry.STONE, new Head(), CHEAP);
        Abilities.addAbility(MaterialRegistry.IRON, new Head(), RUSTABLE);
        Abilities.addAbility(MaterialRegistry.IRON, new Extra(), MAGNETIC);
        Abilities.addAbility(MaterialRegistry.IRON, new Handle(), MAGNETIC);
        Abilities.addAbility(MaterialRegistry.WOODEN, new Extra(), PHOTOSYNTHESIS);
        Abilities.addAbility(MaterialRegistry.WOODEN, new Handle(), PHOTOSYNTHESIS);
    }
}