package com.NovumScientiaTeam.modulartoolkit.modifiers;

import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Modifiers {
    private static final HashMap<Item, AbstractModifier> MAP = new HashMap<>();

    public static void addModifier(Item item, AbstractModifier modifier) {
        MAP.put(item, modifier);
    }

    public static void removeModifierOf(Item item) {
        MAP.remove(item);
    }

    public static AbstractModifier getFor(Item item) {
        return MAP.get(item);
    }

    public static Set<AbstractModifier> getAll() {
        return new HashSet<>(MAP.values());
    }
}