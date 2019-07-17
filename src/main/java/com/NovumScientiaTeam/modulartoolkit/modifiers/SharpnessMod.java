package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.tools.ModularTool;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.item.ItemStack;

public class SharpnessMod extends AbstractModifier {
    public SharpnessMod() {
        super("sharpness");
        addAdditionRequirements(stack -> ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_MELEE_WEAPON) || ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_HOE));
    }

    @Override
    public int getLevelCap() {
        return 50;
    }

    @Override
    public int getLevelRequirement(int level) {
        return 16 * (int) Math.pow(level, 2);
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