package com.NovumScientiaTeam.modulartoolkit.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ToolRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ToolRecipe> {
    public ToolRecipeSerializer() {
        setRegistryName("modulartoolkit:tool_crafting");
    }

    private static NonNullList<Ingredient> readIngredients(JsonArray p_199568_0_) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for (int i = 0; i < p_199568_0_.size(); ++i) {
            Ingredient ingredient = Ingredient.deserialize(p_199568_0_.get(i));
            if (!ingredient.hasNoMatchingItems()) {
                nonnulllist.add(ingredient);
            }
        }

        return nonnulllist;
    }

    @Override
    public ToolRecipe read(ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
        NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (nonnulllist.size() > 3 * 3) {
            throw new JsonParseException("Too many ingredients for shapeless recipe the max is 9");
        } else {
            ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            CompoundNBT mainCompound = new CompoundNBT();
            mainCompound.put("Materials", new CompoundNBT());
            mainCompound.putLong("XP", 0);
            mainCompound.putInt("Level", 0);
            mainCompound.put("Modifiers", new CompoundNBT());
            CompoundNBT boost = new CompoundNBT();
            boost.putInt("Tier", 0);
            boost.putIntArray("OnLevels", new int[0]);
            mainCompound.put("Boost", boost);
            mainCompound.putInt("Damage", 0);
            mainCompound.putInt("ModifierSlotsUsed", 0);
            itemstack.setTag(mainCompound);
            return new ToolRecipe(recipeId, s, itemstack, nonnulllist);
        }
    }

    @Override
    public ToolRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        String s = buffer.readString(32767);
        int i = buffer.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

        for (int j = 0; j < nonnulllist.size(); ++j) {
            nonnulllist.set(j, Ingredient.read(buffer));
        }

        ItemStack itemstack = buffer.readItemStack();
        return new ToolRecipe(recipeId, s, itemstack, nonnulllist);
    }

    @Override
    public void write(PacketBuffer buffer, ToolRecipe recipe) {
        buffer.writeString(recipe.getGroup());
        buffer.writeVarInt(recipe.getIngredients().size());

        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.write(buffer);
        }

        buffer.writeItemStack(recipe.getRecipeOutput());
    }
}