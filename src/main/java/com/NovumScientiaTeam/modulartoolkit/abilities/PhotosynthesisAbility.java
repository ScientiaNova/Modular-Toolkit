package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.NovumScientiaTeam.modulartoolkit.tools.ModularTool;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Random;

public class PhotosynthesisAbility extends AbstractAbility {
    @Override
    public ITextComponent getTranslationKey(ItemStack stack) {
        if (stack.getItem() instanceof ModularTool)
            return new StringTextComponent(TextFormatting.GREEN + new TranslationTextComponent("ability.photosynthesis").getString() + " " + getLevel(stack));
        return new TranslationTextComponent("ability.photosynthesis").applyTextStyle(TextFormatting.GREEN);
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
        if (stack.isDamaged() && !ToolUtils.isBroken(stack) && new Random().nextInt(2000) > getLevel(stack) && worldIn.getLight(entityIn.getPosition()) - 7 > 0)
            ToolUtils.repairTool(stack);
    }
}
