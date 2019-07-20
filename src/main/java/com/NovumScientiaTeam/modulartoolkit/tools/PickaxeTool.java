package com.NovumScientiaTeam.modulartoolkit.tools;

import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.item.ItemStack;

public class PickaxeTool extends ModularTool {
    public PickaxeTool() {
        super("modulartoolkit:pickaxe_tool");
        addToolTags(ToolUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return 0;
        return ToolUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 1;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return -2.8;
    }
}