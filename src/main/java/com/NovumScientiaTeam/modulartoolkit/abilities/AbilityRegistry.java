package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.EmosewaPixel.pixellib.materialsystem.MaterialRegistry;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.*;

public class AbilityRegistry {
    public static final SoftAbility SOFT = new SoftAbility();
    public static final CheapAbility CHEAP = new CheapAbility();
    public static final RustableAbility RUSTABLE = new RustableAbility();
    public static final PhotosynthesisAbility PHOTOSYNTHESIS = new PhotosynthesisAbility();
    public static final MagneticAbility MAGNETIC = new MagneticAbility();
    public static final FlammableAbility FLAMMABLE = new FlammableAbility();
    public static final CobblingAbility COBBLING = new CobblingAbility();

    public static void register() {
        Abilities.addAbility(MaterialRegistry.GOLD, new Head(), SOFT);
        Abilities.addAbility(MaterialRegistry.GOLD, new Extra(), SOFT);
        Abilities.addAbility(MaterialRegistry.GOLD, new Handle(), SOFT);
        Abilities.addAbility(MaterialRegistry.GOLD, new Plating(), SOFT);
        Abilities.addAbility(MaterialRegistry.GOLD, new Frame(null), SOFT);
        Abilities.addAbility(MaterialRegistry.STONE, new Head(), CHEAP);
        Abilities.addAbility(MaterialRegistry.STONE, new Extra(), COBBLING);
        Abilities.addAbility(MaterialRegistry.STONE, new Handle(), COBBLING);
        Abilities.addAbility(MaterialRegistry.IRON, new Head(), RUSTABLE);
        Abilities.addAbility(MaterialRegistry.IRON, new Extra(), MAGNETIC);
        Abilities.addAbility(MaterialRegistry.IRON, new Handle(), MAGNETIC);
        Abilities.addAbility(MaterialRegistry.WOODEN, new Head(), FLAMMABLE);
        Abilities.addAbility(MaterialRegistry.WOODEN, new Extra(), PHOTOSYNTHESIS);
        Abilities.addAbility(MaterialRegistry.WOODEN, new Handle(), PHOTOSYNTHESIS);
    }
}