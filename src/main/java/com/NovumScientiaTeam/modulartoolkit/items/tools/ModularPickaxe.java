package com.NovumScientiaTeam.modulartoolkit.items.tools;

import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.item.ItemStack;

public class ModularPickaxe extends ModularTool {
    public ModularPickaxe() {
        super("modulartoolkit:pickaxe_tool");
        addToolTags(ModularUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return 0;
        return ModularUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 1;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return -2.8;
    }
}