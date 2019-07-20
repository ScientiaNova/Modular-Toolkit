package com.NovumScientiaTeam.modulartoolkit.tools;

import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

public class AxeTool extends ModularTool {
    public AxeTool() {
        super("modulartoolkit:axe_tool");
        addToolTags(ToolUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return 0;
        return ToolUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 6;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return -3.4F;
        return -3.4F + (ToolUtils.getToolMaterial(stack, 0).getItemTier().getEfficiency() / 2 - 1) * 0.1F;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (ToolUtils.isBroken(stack) || ToolUtils.isNull(stack))
            return ActionResultType.PASS;
        ActionResultType result = Items.DIAMOND_AXE.onItemUse(context);
        if (result == ActionResultType.SUCCESS)
            ToolUtils.addXP(stack, context.getPlayer());
        return result;
    }
}