package com.NovumScientiaTeam.modulartoolkit.recipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;

public class SerializerRegistry {
    public static IRecipeSerializer<?> TOOL_RECIPE_SERAILIZER;

    public static void registry(RegistryEvent.Register<IRecipeSerializer<?>> e) {
        TOOL_RECIPE_SERAILIZER = register(new ToolRecipeSerializer(), e);
    }

    private static <T extends IRecipe<?>> IRecipeSerializer<T> register(IRecipeSerializer<T> serializer, RegistryEvent.Register<IRecipeSerializer<?>> e) {
        e.getRegistry().register(serializer);
        return serializer;
    }
}
