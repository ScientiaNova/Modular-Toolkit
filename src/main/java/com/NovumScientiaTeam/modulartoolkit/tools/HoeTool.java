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

public class HoeTool extends ModularTool {
    public HoeTool() {
        super("modulartoolkit:hoe_tool", ImmutableList.of(new Handle(), new Extra(), new Head(null)));
        addToolTags(ToolUtils.IS_HOE);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return -3;
        return ToolUtils.getToolMaterial(stack, 2).getItemTier().getHarvestLevel() + -3;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return Items.DIAMOND_HOE.onItemUse(context);
    }
}