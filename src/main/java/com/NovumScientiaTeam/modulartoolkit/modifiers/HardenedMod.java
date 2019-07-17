package com.NovumScientiaTeam.modulartoolkit.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class HardenedMod extends AbstractModifier {
    public HardenedMod() {
        super("hardened");
    }

    @Override
    public ITextComponent getTextComponent(ItemStack stack, ModifierStats stats) {
        if (stats.getTier() == 10)
            return new TranslationTextComponent("modifier.unbreakable");
        return super.getTextComponent(stack, stats).applyTextStyle(TextFormatting.DARK_PURPLE);
    }

    @Override
    public int getLevelCap() {
        return 10;
    }

    @Override
    public int getLevelRequirement(int level) {
        return 10 * (int) Math.pow(level, 2);
    }

    @Override
    public int onToolDamaged(int amount, ItemStack stack, int tier) {
        if (new Random().nextInt(10) < tier)
            amount = 0;
        return amount;
    }
}