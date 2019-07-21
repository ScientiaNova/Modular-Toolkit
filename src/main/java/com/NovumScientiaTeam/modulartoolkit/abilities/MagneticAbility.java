package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class MagneticAbility extends AbstractAbility {
    @Override
    public ITextComponent getTranslationKey(ItemStack stack) {
        if (stack.getItem() instanceof ModularItem)
            return new StringTextComponent(new TranslationTextComponent("ability.magnetic").getString() + " " + getLevel(stack));
        return new TranslationTextComponent("ability.magnetic");
    }

    @Override
    public int getLevelCap() {
        return 5;
    }

    @Override
    public int getLevel(ItemStack stack) {
        int level = 1 + ModularUtils.getLevel(stack) / 4;
        return ModularUtils.getLevel(stack) > getLevelCap() ? getLevelCap() : level;
    }

    @Override
    public void onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!worldIn.isRemote)
            entityLiving.addPotionEffect(new EffectInstance(ModularToolkit.MAGNETIC, 20, getLevel(stack), false, false));
    }

    @Override
    public void onHitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.world.isRemote && !target.isAlive())
            attacker.addPotionEffect(new EffectInstance(ModularToolkit.MAGNETIC, 20, getLevel(stack), false, false));
    }
}