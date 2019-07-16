package com.NovumScientiaTeam.modulartoolkit.modifiers;

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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public abstract class AbstractModifier {
    private String name;

    public AbstractModifier(String name) {
        this.name = name;
    }

    public ITextComponent getTextComponent(ItemStack stack, ModifierStats stats) {
        int tier = stats.getTier();
        int itemsForCurrent = getLevelRequirement(tier);
        int itemsForLast = getLevelRequirement(tier - 1);
        String lastPart = canLevelUp(stack, tier + 1) ? (stats.getConsumed() - itemsForCurrent) + "/" + (getLevelRequirement(tier + 1) - itemsForCurrent) : (stats.getConsumed() - itemsForLast) + "/" + (itemsForCurrent - itemsForLast);
        return new StringTextComponent(TextFormatting.WHITE + new TranslationTextComponent("modifier." + name).getString() + " " + tier + " (" + lastPart + ")");
    }

    public abstract int getLevelCap();

    public boolean canLevelUp(ItemStack stack, int level) {
        return level <= getLevelCap() && level <= ToolUtils.getLevel(stack);
    }

    //Returns the amount of items that need to be consumed for a certain level
    public abstract int getLevelRequirement(int level);

    public String getName() {
        return name;
    }

    public boolean canBeAdded(ItemStack stack) {
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

    public double setAttackDamage(ItemStack stack, int level, double amount) {
        return amount;
    }

    public float setEfficiency(ItemStack stack, int level, float amount, ToolType type) {
        return amount;
    }

    public double setAttackSpeed(ItemStack stack, int level, double amount) {
        return amount;
    }

    public long onXPAdded(ItemStack stack, int level, long amount) {
        return amount;
    }
}