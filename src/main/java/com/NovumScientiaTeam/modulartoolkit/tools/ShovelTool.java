package com.NovumScientiaTeam.modulartoolkit.tools;

import com.NovumScientiaTeam.modulartoolkit.partTypes.Extra;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Handle;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.ToolType;

public class ShovelTool extends ModularTool {
    public ShovelTool() {
        super("modulartoolkit:shovel_tool", ImmutableList.of(new Head(ToolType.SHOVEL), new Extra(), new Handle()));
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