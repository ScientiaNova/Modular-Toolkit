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
    private final IDrawable patternButton;

    public PartConstructorCategory(IGuiHelper helper) {
        background = helper.createDrawable(PartConstructorScreen.background, 7, 21, 162, 44);
        icon = helper.createDrawableIngredient(new ItemStack(BlockRegistry.PART_CONSTRUCTOR));
        patternButton = helper.createDrawable(PartConstructorScreen.background, 182, 1, 6, 6);
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
        byte[][] pattern = PartRecipe.centerPatern(recipe.getPattern());
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
                if (pattern[i][j] == 1)
                    patternButton.draw(47 + j * 6, 1 + i * 6);
    }

    @Override
    public void setIngredients(PartRecipe partRecipe, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(partRecipe.getInput()));
        iIngredients.setOutputLists(VanillaTypes.ITEM, Arrays.asList(partRecipe.getOutput(), partRecipe.getByproduct()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PartRecipe partRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup guiStacks = recipeLayout.getItemStacks();
        guiStacks.init(0, true, 0, 13);
        guiStacks.init(1, false, 122, 13);
        guiStacks.init(2, false, 144, 13);
        guiStacks.set(iIngredients);
    }
}
