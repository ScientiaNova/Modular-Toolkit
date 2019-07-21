package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ToolType;

public class HasteMod extends AbstractModifier {
    public HasteMod() {
        super("haste");
        addAdditionRequirements(stack -> ((ModularItem) stack.getItem()).hasTag(ModularUtils.IS_TOOL));
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
    public float setEfficiency(ItemStack stack, int level, float amount, ToolType type) {
        return amount * (level + 1);
    }

    @Override
    public TextFormatting getFormatting() {
        return TextFormatting.RED;
    }
}