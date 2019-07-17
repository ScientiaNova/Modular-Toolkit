package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RevivingMod extends AbstractModifier {
    public RevivingMod() {
        super("reviving");
        addAdditionRequirements(stack -> !ToolUtils.getAllModifiers(stack).contains(ModifierRegistry.HARDENED));
    }

    @Override
    public ITextComponent getNameTextComponent(ItemStack stack, ModifierStats stats) {
        return new TranslationTextComponent("modifier.reviving.name");
    }

    @Override
    public int getLevelCap() {
        return 1;
    }

    @Override
    public int getLevelRequirement(int level) {
        return level > 0 ? 1 : 0;
    }

    @Override
    public void onInventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, int tier) {
        if (isSelected && entityIn instanceof PlayerEntity) {
            float hp = ((PlayerEntity) entityIn).getHealth();
            if (hp < 2) {
                float amount = (int) Math.min((stack.getMaxDamage() - 1 - stack.getDamage()) / 50f, ((PlayerEntity) entityIn).getMaxHealth() - hp);
                ((PlayerEntity) entityIn).heal(amount);
                stack.damageItem((int) (amount * 50), (LivingEntity) entityIn, e -> {
                });
            }
        }
    }

    @Override
    public TextFormatting getFormatting() {
        return TextFormatting.GOLD;
    }
}