package com.NovumScientiaTeam.modulartoolkit.items.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public interface IWrenchableBlock {
    ActionResultType whenWrenched(ItemStack stack, ItemUseContext context);
}
