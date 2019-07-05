package com.NovumScientiaTeam.modulartoolkit.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ToolRecipe extends ShapelessRecipe {
    public ToolRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
    }

    public IRecipeSerializer<?> getSerializer() {
        return SerializerRegistry.TOOL_RECIPE_SERAILIZER;
    }
}
