package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.PartType;
import com.google.common.collect.HashBasedTable;

import java.util.HashSet;
import java.util.Set;

public final class Abilities {
    private static final HashBasedTable<Material, String, AbstractAbility> TABLE = HashBasedTable.create();

    public static void addAbility(Material mat, PartType type, AbstractAbility ability) {
        TABLE.put(mat, type.getName(), ability);
    }

    public static void removeAbility(Material mat, PartType type) {
        TABLE.remove(mat, type.getName());
    }

    public static AbstractAbility getFor(Material mat, PartType type) {
        return TABLE.get(mat, type.getName());
    }

    public static Set<AbstractAbility> getAll() {
        return new HashSet<>(TABLE.values());
    }
}