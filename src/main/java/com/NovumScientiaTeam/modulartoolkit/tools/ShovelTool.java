package com.NovumScientiaTeam.modulartoolkit.tools;

import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

public class ShovelTool extends ModularTool {
    public ShovelTool() {
        super("modulartoolkit:shovel_tool");
        addToolTags(ToolUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return 0;
        return ToolUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 1.5f;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return -3;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (ToolUtils.isBroken(stack) || ToolUtils.isNull(stack))
            return ActionResultType.PASS;
        ActionResultType result = Items.DIAMOND_SHOVEL.onItemUse(context);
        if (result == ActionResultType.SUCCESS)
            ToolUtils.addXP(stack, context.getPlayer());
        return result;
    }
}