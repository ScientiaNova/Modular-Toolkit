package com.NovumScientiaTeam.modulartoolkit.tools;

import com.NovumScientiaTeam.modulartoolkit.partTypes.Handle;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.ToolType;

public class PaxelTool extends ModularTool {
    public PaxelTool() {
        super("modulartoolkit:paxel_tool", ImmutableList.of(new Handle(), new Head(ToolType.AXE), new Head(ToolType.SHOVEL), new Head(ToolType.PICKAXE)));
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
        return ToolUtils.getAllToolMaterials(stack).stream().mapToDouble(m -> m.getItemTier().getHarvestLevel()).average().orElse(0) * 0.1 - 4;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (Items.DIAMOND_AXE.onItemUse(context) == ActionResultType.SUCCESS) {
            ToolUtils.addXP(stack);
            return ActionResultType.SUCCESS;
        }
        ActionResultType result = Items.DIAMOND_SHOVEL.onItemUse(context);
        if (result == ActionResultType.SUCCESS)
            ToolUtils.addXP(stack);
        return result;
    }
}