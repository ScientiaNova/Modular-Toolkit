package com.NovumScientiaTeam.modulartoolkit.items.tools;

import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import com.NovumScientiaTeam.modulartoolkit.items.util.ToolUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;

public class ModularPaxel extends ModularTool {
    public ModularPaxel() {
        super("modulartoolkit:paxel_tool");
        addToolTags(ModularUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return 0;
        return ToolUtils.getHeadMaterials(stack).stream().mapToDouble(m -> m.getItemTier().getAttackDamage()).sum() + 2;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return ModularUtils.getAllToolMaterials(stack).stream().mapToDouble(m -> m.getItemTier().getHarvestLevel()).average().orElse(0) * 0.1 - 3.9;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (ModularUtils.isBroken(stack) || ModularUtils.isNull(stack))
            return ActionResultType.PASS;
        if (Items.DIAMOND_AXE.onItemUse(context) == ActionResultType.SUCCESS) {
            ModularUtils.addXP(stack, context.getPlayer());
            return ActionResultType.SUCCESS;
        }
        ActionResultType result = Items.DIAMOND_SHOVEL.onItemUse(context);
        if (result == ActionResultType.SUCCESS)
            ModularUtils.addXP(stack, context.getPlayer());
        return result;
    }
}