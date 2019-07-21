package com.NovumScientiaTeam.modulartoolkit.items.util;

import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.PartType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ToolUtils {
    public static List<Material> getHeadMaterials(ItemStack stack) {
        List<PartType> partList = ModularUtils.getPartList(stack.getItem());
        return IntStream.range(0, partList.size()).filter(i -> partList.get(i) instanceof Head).mapToObj(i -> ModularUtils.getToolMaterial(stack, i)).collect(Collectors.toList());
    }

    public static Map<ToolType, Integer> getHarvestMap(ItemStack stack) {
        List<PartType> partList = ModularUtils.getPartList(stack.getItem());
        return IntStream.range(0, partList.size()).filter(i -> partList.get(i) instanceof Head && ((Head) partList.get(i)).getToolType().isPresent()).boxed().collect(Collectors.toMap(i -> ((Head) partList.get(i)).getToolType().get(), i -> ModularUtils.getToolMaterial(stack, i).getItemTier().getHarvestLevel(), (i1, i2) -> i1 > i2 ? i1 : i2));
    }

    public static float getDestroySpeedForToolType(ItemStack stack, ToolType type) {
        List<PartType> partList = ModularUtils.getPartList(stack.getItem());
        float speed = (float) IntStream.range(0, partList.size()).filter(i -> partList.get(i) instanceof Head && ((Head) partList.get(i)).getToolType().isPresent() && ((Head) partList.get(i)).getToolType().get() == type).mapToDouble(i -> ModularUtils.getToolMaterial(stack, i).getItemTier().getEfficiency()).max().orElse(1);
        for (Map.Entry<AbstractModifier, Integer> e : ModularUtils.getModifierTierMap(stack).entrySet())
            speed = e.getKey().setEfficiency(stack, e.getValue(), speed, type);
        for (AbstractAbility ability : ModularUtils.getAllAbilities(stack))
            speed = ability.setEfficiency(stack, speed, type);
        return (float) (speed * ModularUtils.getBoostMultiplier(stack));
    }
}