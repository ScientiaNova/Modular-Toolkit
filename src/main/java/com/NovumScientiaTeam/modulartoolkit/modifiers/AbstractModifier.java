package com.NovumScientiaTeam.modulartoolkit.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractModifier {
    private String name;

    public AbstractModifier(String name) {
        this.name = name;
    }

    public abstract ITextComponent getTextComponent(ItemStack stack, int level);

    public abstract int getLevelCap();

    public abstract int getLevelUpRequirement(int level);

    public String getName() {
        return name;
    }

    public int onToolDamaged(int amount, int tier) {
        return amount;
    }
}