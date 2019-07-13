package com.NovumScientiaTeam.modulartoolkit.jei.partConstructor.recipetransfer;

import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AdvancedRecipeTransferUtils {
    public static int[] getMatchingItems(Map<Integer, ItemStack> availableItemStacks, List<ItemStack> ingredients) {
        Map<Integer, ItemStack> matchingStacks = ingredients.stream().map(s -> {
            AtomicInteger stackCounter = new AtomicInteger(64);
            return availableItemStacks.entrySet().stream().filter(e -> e.getValue().isItemEqual(s)).filter(entry -> stackCounter.getAndAdd(-entry.getValue().getCount()) > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }).max(Comparator.comparingInt(m -> m.values().stream().mapToInt(ItemStack::getCount).sum())).orElse(Collections.EMPTY_MAP);

        if (matchingStacks.values().stream().reduce(0, (sum, s) -> sum + s.getCount(), Integer::sum) >= ingredients.get(0).getCount())
            return matchingStacks.keySet().stream().mapToInt(Integer::intValue).toArray();

        return new int[0];
    }
}