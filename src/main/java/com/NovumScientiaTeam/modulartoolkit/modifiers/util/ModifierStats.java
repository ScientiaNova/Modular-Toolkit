package com.NovumScientiaTeam.modulartoolkit.modifiers.util;

import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;
import com.NovumScientiaTeam.modulartoolkit.modifiers.Modifiers;
import net.minecraft.nbt.CompoundNBT;

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

    public static ModifierStats deserialize(CompoundNBT nbt) {
        return new ModifierStats(Modifiers.get(nbt.getString("name")), nbt.getInt("tier"), nbt.getInt("consumed"), nbt.getInt("added"));
    }

    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("name", modifier.getName());
        nbt.putInt("tier", tier);
        nbt.putInt("consumed", consumed);
        nbt.putInt("added", added);
        return nbt;
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

    public void setModifier(AbstractModifier modifier) {
        this.modifier = modifier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public void setConsumed(int consumed) {
        this.consumed = consumed;
    }

    public void setAdded(int added) {
        this.added = added;
    }
}
