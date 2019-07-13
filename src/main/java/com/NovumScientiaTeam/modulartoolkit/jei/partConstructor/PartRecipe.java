package com.NovumScientiaTeam.modulartoolkit.jei.partConstructor;

import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPattern;
import com.NovumScientiaTeam.modulartoolkit.tables.screens.PartConstructorScreen;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class PartRecipe {
    private ConstructorPattern pattern;
    private List<ItemStack> input;
    private ItemStack output;
    private ItemStack byproduct;
    private final IDrawable on;
    private final IDrawable off;

    public PartRecipe(ConstructorPattern pattern, List<ItemStack> input, ItemStack output, ItemStack byproduct, IGuiHelper helper) {
        this.pattern = pattern;
        this.input = input;
        this.output = output;
        this.byproduct = byproduct;
        off = helper.createDrawable(new ResourceLocation(PartConstructorScreen.background), 176, 1, 5, 5);
        on = helper.createDrawable(new ResourceLocation(PartConstructorScreen.background), 181, 1, 5, 5);
    }

    public int[][] getPattern() {
        return this.pattern.pattern;
    }

    public static int[][] centerPatern(int[][] pattern) {
        int height = pattern.length;
        int width = pattern[0].length;
        int startTop = (7 - height) / 2;
        int startLeft = (7 - width) / 2;
        int[][] result = new int[7][7];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                result[startTop + i][startLeft + j] = pattern[i][j];

        return result;

    }

    public IDrawable getOnButton() {
        return on;
    }

    public IDrawable getOffButton() {
        return off;
    }

    public List<ItemStack> getInput() {
        return input;
    }

    public List<ItemStack> getOutput() {
        return Collections.singletonList(output);
    }

    public List<ItemStack> getByproduct() {
        if (byproduct.isEmpty())
            return Collections.EMPTY_LIST;
        return Collections.singletonList(byproduct);
    }
}