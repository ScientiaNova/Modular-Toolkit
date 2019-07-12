package com.NovumScientiaTeam.modulartoolkit.abilities;

import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public abstract class AbstractAbility {
    public abstract ITextComponent getTranslationKey(ItemStack stack);

    public abstract int getLevelCap();

    public int getLevel(ItemStack stack) {
        return ToolUtils.getLevel(stack) > getLevelCap() ? getLevelCap() : ToolUtils.getLevel(stack);
    }

    public int onToolDamaged(ItemStack stack, int amount) {
        return amount;
    }

    public int onToolRepaired(int amount, ItemStack stack, int tier) {
        return amount;
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack, Multimap<String, AttributeModifier> multimap) {
        return multimap;
    }

    public void onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {

    }

    public void onHitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {

    }

    public void onInventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

    }
}