package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.tools.ModularTool;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TextFormatting;

public class GlowingMod extends AbstractModifier {
    public GlowingMod() {
        super("glowing");
        addAdditionRequirements(stack -> ((ModularTool) stack.getItem()).hasTag(ToolUtils.IS_MELEE_WEAPON));
    }

    @Override
    public int getLevelCap() {
        return 4;
    }

    @Override
    public int getLevelRequirement(int level) {
        return 30 * (int) Math.pow(level, 2);
    }

    @Override
    public void onHitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker, int tier) {
        if (target.isAlive() && tier > 0)
            target.addPotionEffect(new EffectInstance(Effects.GLOWING, 5 * tier));
    }

    @Override
    public TextFormatting getFormatting() {
        return TextFormatting.YELLOW;
    }
}