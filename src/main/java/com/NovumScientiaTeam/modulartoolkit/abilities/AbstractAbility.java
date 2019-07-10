package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractAbility {
    public abstract ITextComponent getTranslationKey(ItemStack stack);

    public abstract int getLevelCap();

    public int getLevel(ItemStack stack) {
        int toollevel = ToolUtils.getLevel(stack);
        if (toollevel > getLevelCap())
            return toollevel - getLevelCap();
        return ToolUtils.getLevel(stack);
    }

    public int onToolDamaged(ItemStack stack, int amount) {
        return amount;
    }
}