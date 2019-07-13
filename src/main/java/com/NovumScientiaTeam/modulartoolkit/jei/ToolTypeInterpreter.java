package com.NovumScientiaTeam.modulartoolkit.jei;

import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import net.minecraft.item.ItemStack;

public class ToolTypeInterpreter implements ISubtypeInterpreter {
    @Override
    public String apply(ItemStack itemStack) {
        if (ToolUtils.isNull(itemStack))
            return NONE;
        return itemStack.getTag().getCompound("Materials").getString("material0");
    }
}
