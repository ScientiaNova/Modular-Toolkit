package com.NovumScientiaTeam.modulartoolkit.recipes;

import com.EmosewaPixel.pixellib.materialsystem.lists.ObjTypes;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToolRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ToolRecipe> {
    public ToolRecipeSerializer() {
        setRegistryName("modulartoolkit:tool_crafting");
    }

    private static List<Tag<Item>> getTags(JsonArray array) {
        List<Tag<Item>> list = new ArrayList<>();
        array.forEach(element -> list.add(ItemTags.getCollection().get(new ResourceLocation(JSONUtils.getString(element.getAsJsonObject(), "tag")))));

        return list;
    }

    @Override
    public ToolRecipe read(ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        List<Tag<Item>> tags = getTags(JSONUtils.getJsonArray(json, "ingredients"));
        tags.forEach(tag -> nonnulllist.add(Ingredient.fromTag(tag)));
        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for tool recipe");
        } else if (nonnulllist.size() > 3 * 3) {
            throw new JsonParseException("Too many ingredients for tool recipe the max is 9");
        } else {
            ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            ToolUtils.setToolParts(itemstack.getItem(), tags.stream().map(tag -> ObjTypes.get(tag.getId().getPath().substring(0, tag.getId().getPath().length() - 1))).collect(Collectors.toList()));
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