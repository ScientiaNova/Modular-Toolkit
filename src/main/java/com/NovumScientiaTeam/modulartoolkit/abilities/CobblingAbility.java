package com.NovumScientiaTeam.modulartoolkit.abilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class CobblingAbility extends AbstractAbility {
    @Override
    public ITextComponent getTranslationKey(ItemStack stack) {
        return new TranslationTextComponent("ability.cobbling").applyTextStyle(TextFormatting.GRAY);
    }

    @Override
    public int getLevelCap() {
        return 1;
    }

    @Override
    public int getLevel(ItemStack stack) {
        return 1;
    }

    @Override
    public int onToolDamaged(ItemStack stack, int amount) {
        return amount * Math.max(1, (int) (stack.getDamage() / (float) stack.getMaxDamage() * 4));
    }
}
