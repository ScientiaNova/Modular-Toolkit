package com.NovumScientiaTeam.modulartoolkit.items.tools;

import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.item.ItemStack;

public class ModularHammer extends ModularTool {
    public ModularHammer() {
        super("modulartoolkit:hammer_tool");
        addToolTags(ModularUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return 0;
        return ModularUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 4;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return -3.4;
    }

    @Override
    public int getAOERange() {
        return 2;
    }
}