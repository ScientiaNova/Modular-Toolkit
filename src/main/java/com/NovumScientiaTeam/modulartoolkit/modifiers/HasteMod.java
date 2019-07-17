package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.tools.ModularTool;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ToolType;

public class HasteMod extends AbstractModifier {
    public HasteMod() {
        super("haste");
    }

    @Override
    public ITextComponent getTextComponent(ItemStack stack, ModifierStats stats) {
        return super.getTextComponent(stack, stats).applyTextStyle(TextFormatting.RED);
    }

    @Override
    public int getLevelCap() {
        return 10;
    }

    @Override
    public int getLevelRequirement(int level) {
        return 20 * (int) Math.pow(level, 2);
    }

    @Override
    public boolean canBeAdded(ItemStack stack) {
        return ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_TOOL);
    }

    @Override
    public float setEfficiency(ItemStack stack, int level, float amount, ToolType type) {
        return amount * (level + 1);
    }
}