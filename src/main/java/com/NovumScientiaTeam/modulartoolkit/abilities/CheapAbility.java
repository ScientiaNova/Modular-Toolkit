package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class CheapAbility extends AbstractAbility {
    @Override
    public ITextComponent getTranslationKey(ItemStack stack) {
        if (stack.getItem() instanceof ModularItem)
            return new StringTextComponent(TextFormatting.GRAY + new TranslationTextComponent("ability.cheap").getString() + " " + getLevel(stack));
        return new TranslationTextComponent("ability.cheap").applyTextStyle(TextFormatting.GRAY);
    }

    @Override
    public int getLevelCap() {
        return 4;
    }

    @Override
    public int getLevel(ItemStack stack) {
        int level = ModularUtils.getLevel(stack) / 2 + 1;
        return level > getLevelCap() ? getLevelCap() : level;
    }

    @Override
    public int onToolRepaired(ItemStack stack, int amount) {
        return (int) (amount * 1.1);
    }
}
