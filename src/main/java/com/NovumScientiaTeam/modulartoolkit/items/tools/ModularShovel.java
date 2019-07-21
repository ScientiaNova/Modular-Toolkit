package com.NovumScientiaTeam.modulartoolkit.items.tools;

import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

public class ModularShovel extends ModularTool {
    public ModularShovel() {
        super("modulartoolkit:shovel_tool");
        addToolTags(ModularUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return 0;
        return ModularUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 1.5f;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return -3;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (ModularUtils.isBroken(stack) || ModularUtils.isNull(stack))
            return ActionResultType.PASS;
        ActionResultType result = Items.DIAMOND_SHOVEL.onItemUse(context);
        if (result == ActionResultType.SUCCESS)
            ModularUtils.addXP(stack, context.getPlayer());
        return result;
    }
}