package com.NovumScientiaTeam.modulartoolkit.modifiers;

public class ModifierStats {
    private AbstractModifier modifier;
    private int tier;
    private int consumed;
    private int added;

    public ModifierStats(AbstractModifier modifier, int tier, int consumed, int added) {
        this.modifier = modifier;
        this.tier = tier;
        this.consumed = consumed;
        this.added = added;
    }

    public AbstractModifier getModifier() {
        return modifier;
    }

    public int getTier() {
        return tier;
    }

    public int getConsumed() {
        return consumed;
    }

    public int getAdded() {
        return added;
    }
}
