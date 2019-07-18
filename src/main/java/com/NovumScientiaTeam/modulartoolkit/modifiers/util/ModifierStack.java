package com.NovumScientiaTeam.modulartoolkit.modifiers.util;

import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;

public class ModifierStack {
    private AbstractModifier modifier;
    private int count;
    public static final ModifierStack EMPTY = new ModifierStack(null, 0);

    public ModifierStack(AbstractModifier modifier, int count) {
        this.modifier = modifier;
        this.count = count;
    }

    public ModifierStack(AbstractModifier modifier) {
        this(modifier, 1);
    }

    public AbstractModifier getModifier() {
        return modifier;
    }

    public void setModifier(AbstractModifier modifier) {
        this.modifier = modifier;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
