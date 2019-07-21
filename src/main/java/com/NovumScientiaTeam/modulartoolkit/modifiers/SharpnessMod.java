package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.item.ItemStack;

public class SharpnessMod extends AbstractModifier {
    public SharpnessMod() {
        super("sharpness");
        addAdditionRequirements(stack -> ((ModularItem) stack.getItem()).hasTag(ModularUtils.IS_MELEE_WEAPON) || ((ModularItem) stack.getItem()).hasTag(ModularUtils.IS_HOE));
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