package com.NovumScientiaTeam.modulartoolkit.recipes;

import com.EmosewaPixel.pixellib.materialSystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialSystem.materials.IMaterialItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ToolRecipe extends ShapelessRecipe {
    public ToolRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
    }

    public IRecipeSerializer<?> getSerializer() {
        return SerializerRegistry.TOOL_RECIPE_SERAILIZER;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inventory) {
        ItemStack result = getRecipeOutput();
        List<ItemStack> inputs = IntStream.range(0, inventory.getSizeInventory()).mapToObj(inventory::getStackInSlot).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        NonNullList<Ingredient> ingredients = getIngredients();
        AtomicInteger index = new AtomicInteger(0);
        CompoundNBT materialNBT = result.getTag().getCompound("Materials");
        ingredients.stream().map(i -> {
            ItemStack match = inputs.stream().filter(i::test).findFirst().get();
            inputs.remove(match);
            if (match.getItem() instanceof IMaterialItem)
                return ((IMaterialItem) match.getItem()).getMaterial().getName();
            return MaterialItems.getItemMaterial(match.getItem()).getName();
        }).collect(Collectors.toList()).forEach(s -> materialNBT.putString("material" + index.getAndIncrement(), s));
        return result;
    }
}
