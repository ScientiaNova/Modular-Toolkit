package com.NovumScientiaTeam.modulartoolkit.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class EnchantedMod extends AbstractModifier {
    public EnchantedMod() {
        super("enchanted");
    }

    @Override
    public ITextComponent getTextComponent(ItemStack stack, ModifierStats stats) {
        return super.getTextComponent(stack, stats).applyTextStyle(TextFormatting.GREEN);
    }

    @Override
    public int getLevelCap() {
        return 3;
    }

    @Override
    public int getLevelRequirement(int level) {
        return level * (level - 1) + 1;
    }

    @Override
    public long onXPAdded(ItemStack stack, int level, long amount) {
        return amount * level * 2;
    }
}