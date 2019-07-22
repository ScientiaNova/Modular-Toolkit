package com.NovumScientiaTeam.modulartoolkit.items.tools;

import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

public class ModularAxe extends ModularTool {
    public ModularAxe() {
        super("modulartoolkit:axe_tool");
        addToolTags(ModularUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return 0;
        return ModularUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 6;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return -3.4F;
        return -3.4F + (ModularUtils.getToolMaterial(stack, 0).getItemTier().getEfficiency() / 2 - 1) * 0.1F;
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (ModularUtils.isBroken(stack) || ModularUtils.isNull(stack))
            return ActionResultType.PASS;
        ActionResultType result = Items.DIAMOND_AXE.onItemUse(context);
        if (result == ActionResultType.SUCCESS)
            ModularUtils.addXP(stack, context.getPlayer());
        return result;
    }
}