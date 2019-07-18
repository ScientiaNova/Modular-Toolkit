package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.tools.ModularTool;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class LuckMod extends AbstractModifier {
    public LuckMod() {
        super("luck");
        addAdditionRequirements(stack -> ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_MELEE_WEAPON) || ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_TOOL) || ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_HOE));
    }

    @Override
    public int getLevelCap() {
        return 3;
    }

    @Override
    public int getLevelRequirement(int level) {
        return 40 * (int) Math.pow(level, 2);
    }

    @Override
    public void whenGainedLevel(ItemStack stack, int level) {
        if (level > 0)
            ToolUtils.remapEnchantments(stack);
    }

    @Override
    public void whenRemoved(ItemStack stack, int level) {
        if (level > 0)
            stack.removeChildTag("Enchantments");
    }

    @Override
    public void enchantItem(ItemStack stack, int level) {
        if (((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_MELEE_WEAPON) || ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_HOE))
            stack.addEnchantment(Enchantments.LOOTING, level);
        else
            stack.addEnchantment(Enchantments.FORTUNE, level);
    }

    @Override
    public TextFormatting getFormatting() {
        return TextFormatting.BLUE;
    }
}