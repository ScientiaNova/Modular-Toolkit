package com.NovumScientiaTeam.modulartoolkit.recipes;

import com.EmosewaPixel.pixellib.resourceAddition.JSONAdder;
import com.EmosewaPixel.pixellib.resourceAddition.RecipeMaker;
import com.NovumScientiaTeam.modulartoolkit.ObjTypeRegistry;
import com.NovumScientiaTeam.modulartoolkit.tools.ToolRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ToolRecipeRegistry {
    public static void registry() {
        addToolRecipe(new ItemStack(ToolRegistry.PICKAXE), ObjTypeRegistry.TOOL_ROD.getItemTag(), ObjTypeRegistry.BINDING.getItemTag(), ObjTypeRegistry.PICKAXE_HEAD.getItemTag());
        addToolRecipe(new ItemStack(ToolRegistry.AXE), ObjTypeRegistry.TOOL_ROD.getItemTag(), ObjTypeRegistry.BINDING.getItemTag(), ObjTypeRegistry.AXE_HEAD.getItemTag());
        addToolRecipe(new ItemStack(ToolRegistry.SHOVEL), ObjTypeRegistry.TOOL_ROD.getItemTag(), ObjTypeRegistry.BINDING.getItemTag(), ObjTypeRegistry.SHOVEL_HEAD.getItemTag());
        addToolRecipe(new ItemStack(ToolRegistry.HOE), ObjTypeRegistry.TOOL_ROD.getItemTag(), ObjTypeRegistry.BINDING.getItemTag(), ObjTypeRegistry.HOE_HEAD.getItemTag());
        addToolRecipe(new ItemStack(ToolRegistry.SWORD), ObjTypeRegistry.TOOL_ROD.getItemTag(), ObjTypeRegistry.SWORD_GUARD.getItemTag(), ObjTypeRegistry.SWORD_BLADE.getItemTag());
        addToolRecipe(new ItemStack(ToolRegistry.PAXEL), ObjTypeRegistry.TOOL_ROD.getItemTag(), ObjTypeRegistry.AXE_HEAD.getItemTag(), ObjTypeRegistry.SHOVEL_HEAD.getItemTag(), ObjTypeRegistry.PICKAXE_HEAD.getItemTag());
    }

    public static void addToolRecipe(ItemStack output, @Nonnull Object... inputs) {
        ResourceLocation name = output.getItem().getRegistryName();
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "modulartoolkit:tool_crafting");
        recipe.addProperty("group", name.toString());
        JsonArray ingredients = new JsonArray();
        Arrays.stream(inputs).map(RecipeMaker::inputToJsn).forEach(ingredients::add);
        recipe.add("ingredients", ingredients);
        recipe.add("result", RecipeMaker.stackToJson(output));
        JSONAdder.addDataJSON(new ResourceLocation(name.getNamespace(), "recipes/" + name.getPath() + ".json"), recipe);
    }
}
