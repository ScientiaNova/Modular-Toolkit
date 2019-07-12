package com.NovumScientiaTeam.modulartoolkit.modifiers;

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

public abstract class AbstractModifier {
    private String name;

    public AbstractModifier(String name) {
        this.name = name;
    }

    public abstract ITextComponent getTextComponent(ItemStack stack, int level);

    public abstract int getLevelCap();

    public boolean getLevelUpRequirement(ItemStack stack, int level) {
        return level <= getLevelCap();
    }

    public String getName() {
        return name;
    }

    public boolean canBeAdded() {
        return true;
    }

    public int onToolDamaged(int amount, ItemStack stack, int tier) {
        return amount;
    }

    public int onToolRepaired(int amount, ItemStack stack, int tier) {
        return amount;
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack, int tier, Multimap<String, AttributeModifier> multimap) {
        return multimap;
    }

    public void onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving, int tier) {

    }

    public void onHitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker, int tier) {

    }

    public void onInventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, int tier) {

    }
}