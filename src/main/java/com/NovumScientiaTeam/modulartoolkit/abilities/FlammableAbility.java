package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.NovumScientiaTeam.modulartoolkit.tools.ModularTool;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class FlammableAbility extends AbstractAbility {
    @Override
    public ITextComponent getTranslationKey(ItemStack stack) {
        if (stack.getItem() instanceof ModularTool)
            return new StringTextComponent(TextFormatting.GOLD + new TranslationTextComponent("ability.flammable").getString() + " " + getLevel(stack));
        return new TranslationTextComponent("ability.flammable").applyTextStyle(TextFormatting.GOLD);
    }

    @Override
    public int getLevelCap() {
        return 10;
    }

    @Override
    public int getLevel(ItemStack stack) {
        int level = ToolUtils.getLevel(stack) / 2 + 1;
        return level > getLevelCap() ? getLevelCap() : level;
    }

    @Override
    public void onInventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity && !ToolUtils.isBroken(stack) && entityIn.func_223314_ad() > 0 && entityIn.func_223314_ad() % 20 == 0)
            stack.damageItem(1, (LivingEntity) entityIn, e -> { });
    }
}
