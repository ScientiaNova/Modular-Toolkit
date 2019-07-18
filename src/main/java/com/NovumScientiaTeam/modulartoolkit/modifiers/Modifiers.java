package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.modifiers.util.ModifierStack;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Modifiers {
    private static final HashMap<Item, ModifierStack> ITEM_MAP = new HashMap<>();
    private static final HashMap<String, AbstractModifier> MAP = new HashMap<>();

    public static void addModifier(AbstractModifier modifier) {
        MAP.put(modifier.getName(), modifier);
    }

    public static void giveModifier(Item item, ModifierStack stack) {
        ITEM_MAP.put(item, stack);
    }

    public static void removeModifierOf(Item item) {
        ITEM_MAP.remove(item);
    }

    public static AbstractModifier get(String name) {
        return MAP.get(name);
    }

    public static ModifierStack getFor(Item item) {
        return ITEM_MAP.get(item);
    }

    public static Set<AbstractModifier> getAll() {
        return new HashSet<>(MAP.values());
    }
}