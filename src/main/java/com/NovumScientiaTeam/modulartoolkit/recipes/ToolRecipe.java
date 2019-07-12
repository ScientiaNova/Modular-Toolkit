package com.NovumScientiaTeam.modulartoolkit.recipes;

import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
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
        ItemStack result = getRecipeOutput().copy();
        List<ItemStack> inputs = IntStream.range(0, inventory.getSizeInventory()).mapToObj(inventory::getStackInSlot).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        NonNullList<Ingredient> ingredients = getIngredients();
        CompoundNBT mainCompound = new CompoundNBT();
        mainCompound.put("Materials", new CompoundNBT());
        mainCompound.putLong("XP", 0);
        mainCompound.putInt("Level", 0);
        mainCompound.put("Modifiers", new CompoundNBT());
        mainCompound.put("Boosts", new CompoundNBT());
        mainCompound.putInt("Damage", 0);
        mainCompound.putInt("ModifierSlotsUsed", 0);
        AtomicInteger index = new AtomicInteger(0);
        CompoundNBT materialNBT = new CompoundNBT();
        ingredients.stream().map(i -> {
            ItemStack match = inputs.stream().filter(i).findFirst().get();
            inputs.remove(match);
            if (match.getItem() instanceof IMaterialItem)
                return ((IMaterialItem) match.getItem()).getMaterial().getName();
            return MaterialItems.getItemMaterial(match.getItem()).getName();
        }).forEach(s -> materialNBT.putString("material" + index.getAndIncrement(), s));
        mainCompound.put("Materials", materialNBT);
        result.setTag(mainCompound);
        return result;
    }
}