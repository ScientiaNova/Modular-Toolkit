package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class HardenedMod extends AbstractModifier {
    public HardenedMod() {
        super("hardened");
        addAdditionRequirements(stack -> !ToolUtils.getAllModifiers(stack).contains(ModifierRegistry.REVIVING));
    }

    @Override
    public ITextComponent getNameTextComponent(ItemStack stack, ModifierStats stats) {
        if (stats == null || stats.getTier() < 10)
            return super.getNameTextComponent(stack, stats);
        return new TranslationTextComponent("modifier.unbreakable.name");
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

    @Override
    public TextFormatting getFormatting() {
        return TextFormatting.DARK_PURPLE;
    }
}