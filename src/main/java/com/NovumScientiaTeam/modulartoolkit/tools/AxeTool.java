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

public class AxeTool extends ModularTool {
    public AxeTool() {
        super("modulartoolkit:axe_tool", ImmutableList.of(new Handle(), new Extra(), new Head(ToolType.AXE)));
        addToolTags(ToolUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return 0;
        return ToolUtils.getToolMaterial(stack, 2).getItemTier().getAttackDamage() + 6;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return 0;
        return -3.4F + (ToolUtils.getToolMaterial(stack, 2).getItemTier().getEfficiency() / 2 - 1) * 0.1F;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return Items.DIAMOND_AXE.onItemUse(context);
    }
}