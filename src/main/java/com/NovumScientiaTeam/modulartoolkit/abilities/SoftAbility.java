package com.NovumScientiaTeam.modulartoolkit.abilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class SoftAbility extends AbstractAbility {
    @Override
    public ITextComponent getTranslationKey(ItemStack stack) {
        int level = getLevel(stack);
        return new StringTextComponent(TextFormatting.GOLD + new TranslationTextComponent("ability.soft").getString() + (level > 0 ? " " + level : ""));
    }

    @Override
    public int getLevelCap() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int onToolDamaged(ItemStack stack, int amount) {
        if (getLevel(stack) < 1)
            return amount;
        return amount * getLevel(stack);
    }
}
