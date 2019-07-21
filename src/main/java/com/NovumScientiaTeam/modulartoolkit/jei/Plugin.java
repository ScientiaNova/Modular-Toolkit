package com.NovumScientiaTeam.modulartoolkit.jei;

import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialsystem.lists.Materials;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.parts.ObjTypeRegistry;
import com.NovumScientiaTeam.modulartoolkit.jei.partConstructor.PartConstructorCategory;
import com.NovumScientiaTeam.modulartoolkit.jei.partConstructor.PartConstructorTransferHandler;
import com.NovumScientiaTeam.modulartoolkit.jei.partConstructor.PartRecipe;
import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPatternRegistry;
import com.NovumScientiaTeam.modulartoolkit.tables.blocks.BlockRegistry;
import com.NovumScientiaTeam.modulartoolkit.tables.screens.PartConstructorScreen;
import com.NovumScientiaTeam.modulartoolkit.items.ItemRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class Plugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ModularToolkit.MOD_ID);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration reg) {
        ToolTypeInterpreter toolInterpreter = new ToolTypeInterpreter();
        ItemRegistry.TOOLS.forEach(tool -> reg.registerSubtypeInterpreter(tool, toolInterpreter));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(new PartConstructorCategory(reg.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        IGuiHelper helper = reg.getJeiHelpers().getGuiHelper();
        List<Material> toolMaterials = Materials.getAll().stream().filter(m -> m.getItemTier() != null).collect(Collectors.toList());
        List<ObjectType> parts = ConstructorPatternRegistry.getPossibleObjectTypes();
        List<PartRecipe> recipes = new ArrayList<>();
        parts.forEach(part ->
                toolMaterials.forEach(mat -> {
                    float inputCost = part.getBucketVolume() / 144f;
                    recipes.add(new PartRecipe(ConstructorPatternRegistry.getPattern(part), Arrays.stream(mat.getItemTier().getRepairMaterial().getMatchingStacks()).map(s -> new ItemStack(s.getItem(), (int) Math.ceil(inputCost))).collect(Collectors.toList()), new ItemStack(MaterialItems.getItem(mat, part)), inputCost - (int) inputCost == 0.5 ? new ItemStack(MaterialItems.getItem(mat, ObjTypeRegistry.FRAGMENT)) : ItemStack.EMPTY, helper));
                    recipes.add(new PartRecipe(ConstructorPatternRegistry.getPattern(part), Collections.singletonList(new ItemStack(MaterialItems.getItem(mat, ObjTypeRegistry.FRAGMENT), (int) (inputCost * 2))), new ItemStack(MaterialItems.getItem(mat, part)), ItemStack.EMPTY, helper));
                }));
        reg.addRecipes(recipes, PartConstructorCategory.ID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(BlockRegistry.PART_CONSTRUCTOR), PartConstructorCategory.ID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration reg) {
        reg.addRecipeClickArea(PartConstructorScreen.class, 28, 35, 22, 15, PartConstructorCategory.ID);
        reg.addRecipeClickArea(PartConstructorScreen.class, 100, 35, 22, 15, PartConstructorCategory.ID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration reg) {
        reg.addRecipeTransferHandler(new PartConstructorTransferHandler(reg.getTransferHelper()), PartConstructorCategory.ID);
    }
}