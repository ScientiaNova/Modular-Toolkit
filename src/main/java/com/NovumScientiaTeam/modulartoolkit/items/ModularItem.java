package com.NovumScientiaTeam.modulartoolkit.items;

import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;
import com.NovumScientiaTeam.modulartoolkit.parts.PartTypeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils.*;

public abstract class ModularItem extends Item {
    private List<String> toolTags = new ArrayList<>();

    public ModularItem(String name) {
        super(new Properties().group(ModularToolkit.MAIN_GROUP).maxStackSize(1).setNoRepair());
        setRegistryName(name);
        addPropertyOverride(new ResourceLocation("broken"), (stack, world, entity) -> isBroken(stack) ? 1 : 0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!isNull(stack)) {
            if (isBroken(stack))
                tooltip.add(new TranslationTextComponent("tool.stat.broken").applyTextStyle(TextFormatting.RED));
            if (InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT))
                addStats(stack, tooltip, false);
            else if (InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
                List<Material> materials = getAllToolMaterials(stack);
                List<ObjectType> parts = getToolParts(stack.getItem());
                IntStream.range(0, materials.size()).forEach(i -> {
                    Item item = MaterialItems.getItem(materials.get(i), parts.get(i));
                    tooltip.add(new ItemStack(item).getDisplayName().applyTextStyle(TextFormatting.UNDERLINE));
                    PartTypeMap.getPartType(parts.get(i)).addTooltip(item, tooltip);
                });
            } else {
                tooltip.add(new TranslationTextComponent("tool.tooltip.tool_button"));
                tooltip.add(new TranslationTextComponent("tool.tooltip.part_button"));
            }
        }
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (isNull(stack))
            return amount;
        if (amount > 0) {
            for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
                amount = e.getKey().onToolDamaged(amount, stack, e.getValue());
            for (AbstractAbility ability : getAllAbilities(stack))
                amount = ability.onToolDamaged(stack, amount);
        } else {
            for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
                amount = e.getKey().onToolRepaired(amount, stack, e.getValue());
            for (AbstractAbility ability : getAllAbilities(stack))
                amount = ability.onToolRepaired(stack, amount);
        }
        if (amount > 0) {
            int max = getMaxDamage(stack) - 1 - stack.getDamage();
            if (amount >= max && !isBroken(stack))
                makeBroken(stack, entity);
            return Math.min(amount, max);
        } else {
            if (amount < 0 && isBroken(stack))
                unbreak(stack);
            return Math.max(amount, -getDamage(stack));
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return super.showDurabilityBar(stack) && !isBroken(stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
            e.getKey().onInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected, e.getValue());
        for (AbstractAbility ability : getAllAbilities(stack))
            ability.onInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.COMMON;
    }

    public void addToolTags(String... tags) {
        toolTags.addAll(Arrays.asList(tags));
    }

    public boolean hasTag(String tag) {
        return toolTags.contains(tag);
    }

    public abstract void addStats(ItemStack stack, List<ITextComponent> tooltip, boolean compact);
}