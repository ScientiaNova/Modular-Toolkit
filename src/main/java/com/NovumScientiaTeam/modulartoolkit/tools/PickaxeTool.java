package com.NovumScientiaTeam.modulartoolkit.tools;

import com.NovumScientiaTeam.modulartoolkit.partTypes.Extra;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Handle;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

public class PickaxeTool extends ModularTool {
    public PickaxeTool() {
        super("modulartoolkit:pickaxe_tool", ImmutableList.of(new Handle(), new Extra(), new Head(ToolType.PICKAXE)));
        addToolTags(ToolUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return 0;
        return ToolUtils.getToolMaterial(stack, 2).getItemTier().getAttackDamage() + 1;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return -2.8;
    }
}