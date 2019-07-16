package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.tools.ModularTool;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class SharpnessMod extends AbstractModifier {
    public SharpnessMod() {
        super("sharpness");
    }

    @Override
    public ITextComponent getTextComponent(ItemStack stack, ModifierStats stats) {
        return super.getTextComponent(stack, stats).applyTextStyle(TextFormatting.WHITE);
    }

    @Override
    public int getLevelCap() {
        return 50;
    }

    @Override
    public int getLevelRequirement(int level) {
        return 16 * (int) Math.pow(level, 2);
    }

    @Override
    public boolean canBeAdded(ItemStack stack) {
        return ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_MELEE_WEAPON) || ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_HOE);
    }

    public double setAttackDamage(ItemStack stack, int level, double amount) {
        for (int i = 0; i < level; i++) {
            if (i == 0)
                amount += amount * 0.1;
            else
                amount += amount * 0.1 / ((i + 9) / 10);
        }
        return amount;
    }

}