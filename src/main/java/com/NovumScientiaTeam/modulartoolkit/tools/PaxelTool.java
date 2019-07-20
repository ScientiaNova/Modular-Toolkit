package com.NovumScientiaTeam.modulartoolkit.tools;

import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

public class PaxelTool extends ModularTool {
    public PaxelTool() {
        super("modulartoolkit:paxel_tool");
        addToolTags(ToolUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return 0;
        return ToolUtils.getHeadMaterials(stack).stream().mapToDouble(m -> m.getItemTier().getAttackDamage()).sum() + 2;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return ToolUtils.getAllToolMaterials(stack).stream().mapToDouble(m -> m.getItemTier().getHarvestLevel()).average().orElse(0) * 0.1 - 3.9;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (ToolUtils.isBroken(stack) || ToolUtils.isNull(stack))
            return ActionResultType.PASS;
        if (Items.DIAMOND_AXE.onItemUse(context) == ActionResultType.SUCCESS) {
            ToolUtils.addXP(stack, context.getPlayer());
            return ActionResultType.SUCCESS;
        }
        ActionResultType result = Items.DIAMOND_SHOVEL.onItemUse(context);
        if (result == ActionResultType.SUCCESS)
            ToolUtils.addXP(stack, context.getPlayer());
        return result;
    }
}