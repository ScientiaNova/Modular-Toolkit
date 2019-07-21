package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class RustableAbility extends AbstractAbility {
    @Override
    public ITextComponent getTranslationKey(ItemStack stack) {
        if (stack.getItem() instanceof ModularItem)
            return new StringTextComponent(TextFormatting.DARK_RED + new TranslationTextComponent("ability.rustable").getString() + " " + getLevel(stack));
        return new TranslationTextComponent("ability.rustable").applyTextStyle(TextFormatting.DARK_RED);
    }

    @Override
    public int getLevelCap() {
        return 4;
    }

    @Override
    public int getLevel(ItemStack stack) {
        int level = ModularUtils.getLevel(stack) / 3 + 1;
        return level > getLevelCap() ? getLevelCap() : level;
    }

    @Override
    public void onHitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isAlive())
            if (new Random().nextInt(stack.getMaxDamage() * 4) < stack.getDamage())
                target.attackEntityFrom(DamageSource.causeMobDamage(attacker), getLevel(stack) * 2);
    }
}
