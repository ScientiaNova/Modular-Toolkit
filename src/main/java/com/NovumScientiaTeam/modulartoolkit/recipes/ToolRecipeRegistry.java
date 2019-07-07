package com.NovumScientiaTeam.modulartoolkit.recipes;

import com.EmosewaPixel.pixellib.materialSystem.lists.Materials;
import com.EmosewaPixel.pixellib.materialSystem.materials.Material;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ToolRecipeRegistry {
    public static void registry() {
        List<Material> toolMaterials = Materials.getAll().stream().filter(m -> m.getItemTier() != null).collect(Collectors.toList());

        toolMaterials.forEach(m0 ->
                toolMaterials.forEach(m1 ->
                        toolMaterials.forEach(m2 -> {
                            addToolRecipe(new ItemStack(ToolRegistry.PICKAXE), Arrays.asList(m0, m1, m2), m0.getTag(ObjTypeRegistry.TOOL_ROD), m1.getTag(ObjTypeRegistry.BINDING), m2.getTag(ObjTypeRegistry.PICKAXE_HEAD));
                            addToolRecipe(new ItemStack(ToolRegistry.AXE), Arrays.asList(m0, m1, m2), m0.getTag(ObjTypeRegistry.TOOL_ROD), m1.getTag(ObjTypeRegistry.BINDING), m2.getTag(ObjTypeRegistry.AXE_HEAD));
                            addToolRecipe(new ItemStack(ToolRegistry.SHOVEL), Arrays.asList(m0, m1, m2), m0.getTag(ObjTypeRegistry.TOOL_ROD), m1.getTag(ObjTypeRegistry.BINDING), m2.getTag(ObjTypeRegistry.SHOVEL_HEAD));
                            addToolRecipe(new ItemStack(ToolRegistry.HOE), Arrays.asList(m0, m1, m2), m0.getTag(ObjTypeRegistry.TOOL_ROD), m1.getTag(ObjTypeRegistry.BINDING), m2.getTag(ObjTypeRegistry.HOE_HEAD));
                            addToolRecipe(new ItemStack(ToolRegistry.SWORD), Arrays.asList(m0, m1, m2), m0.getTag(ObjTypeRegistry.TOOL_ROD), m1.getTag(ObjTypeRegistry.SWORD_GUARD), m2.getTag(ObjTypeRegistry.SWORD_BLADE));
                            toolMaterials.forEach(m3 -> {
                                addToolRecipe(new ItemStack(ToolRegistry.PAXEL), Arrays.asList(m0, m1, m2, m3), m0.getTag(ObjTypeRegistry.TOOL_ROD), m1.getTag(ObjTypeRegistry.AXE_HEAD), m2.getTag(ObjTypeRegistry.SHOVEL_HEAD), m3.getTag(ObjTypeRegistry.PICKAXE_HEAD));
                            });
                        })));
    }

    public static void addToolRecipe(ItemStack output, List<Material> toolMaterials, @Nonnull Object... inputs) {
        ResourceLocation name = output.getItem().getRegistryName();
        List<String> materialNames = toolMaterials.stream().map(Material::getName).collect(Collectors.toList());
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "modulartoolkit:tool_crafting");
        recipe.addProperty("group", name.toString());
        JsonArray ingredients = new JsonArray();
        Arrays.stream(inputs).map(RecipeMaker::inputToJsn).forEach(ingredients::add);
        recipe.add("ingredients", ingredients);
        recipe.add("result", RecipeMaker.stackToJson(output));
        JsonObject materials = new JsonObject();
        AtomicInteger index = new AtomicInteger(0);
        materialNames.forEach(s -> materials.addProperty("material" + index.getAndIncrement(), s));
        recipe.add("materials", materials);
        JSONAdder.addDataJSON(new ResourceLocation(name.getNamespace(), "recipes/" + name.getPath() + "_" + String.join("_", materialNames) + ".json"), recipe);
    }
}
