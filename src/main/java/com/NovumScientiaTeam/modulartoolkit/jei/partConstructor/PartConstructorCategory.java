package com.NovumScientiaTeam.modulartoolkit.jei.partConstructor;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.tables.blocks.BlockRegistry;
import com.NovumScientiaTeam.modulartoolkit.tables.screens.PartConstructorScreen;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;

public class PartConstructorCategory implements IRecipeCategory<PartRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    public static final ResourceLocation ID = new ResourceLocation(ModularToolkit.MOD_ID, "part_constructor");

    public PartConstructorCategory(IGuiHelper helper) {
        background = helper.createDrawable(new ResourceLocation(PartConstructorScreen.background), 15, 25, 146, 35);
        icon = helper.createDrawableIngredient(new ItemStack(BlockRegistry.PART_CONSTRUCTOR));
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends PartRecipe> getRecipeClass() {
        return PartRecipe.class;
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("gui.jei.category.part_constructor");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(PartRecipe recipe, double mouseX, double mouseY) {
        int[][] pattern = PartRecipe.centerPatern(recipe.getPattern());
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
                if (pattern[i][j] == 0)
                    recipe.getOffButton().draw(23 + j * 5, i * 5);
                else
                    recipe.getOnButton().draw(23 + j * 5, i * 5);
    }

    @Override
    public void setIngredients(PartRecipe partRecipe, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(partRecipe.getInput()));
        iIngredients.setOutputLists(VanillaTypes.ITEM, Arrays.asList(partRecipe.getOutput(), partRecipe.getByproduct()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PartRecipe partRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup guiStacks = recipeLayout.getItemStacks();
        guiStacks.init(0, true, 0, 9);
        guiStacks.init(1, false, 106, 9);
        guiStacks.init(2, false, 128, 9);
        guiStacks.set(iIngredients);
    }
}
