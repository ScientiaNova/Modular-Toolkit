package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class FireyMod extends AbstractModifier {
    public FireyMod() {
        super("firey");
        addAdditionRequirements(stack -> ((ModularItem) stack.getItem()).hasTag(ModularUtils.IS_MELEE_WEAPON));
    }

    @Override
    public int getLevelCap() {
        return 2;
    }

    @Override
    public int getLevelRequirement(int level) {
        return 30 * (int) Math.pow(level, 2);
    }

    @Override
    public void onHitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker, int tier) {
        if (target.isAlive() && !target.isImmuneToFire() && tier > 0)
            target.setFire(4 * tier);
    }

    @Override
    public TextFormatting getFormatting() {
        return TextFormatting.GOLD;
    }
}