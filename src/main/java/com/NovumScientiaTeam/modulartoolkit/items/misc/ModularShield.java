package com.NovumScientiaTeam.modulartoolkit.items.misc;

import com.EmosewaPixel.pixellib.materialsystem.lists.Materials;
import com.EmosewaPixel.pixellib.miscutils.StreamUtils;
import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import com.NovumScientiaTeam.modulartoolkit.parts.ArmorPartType;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.PartType;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.Plating;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils.*;

public class ModularShield extends ModularItem {
    public ModularShield() {
        super("modulartoolkit:modular_shield");
        this.addPropertyOverride(new ResourceLocation("blocking"), (stack, world, entity) ->
                entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1 : 0);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (isNull(stack))
            return new TranslationTextComponent(this.getTranslationKey(stack));

        List<PartType> partList = ModularUtils.getPartList(stack.getItem());

        List<ITextComponent> matStrings = IntStream.range(0, partList.size()).filter(i -> partList.get(i) instanceof Plating).mapToObj(i -> ModularUtils.getToolMaterial(stack, i).getTranslationKey()).distinct().collect(Collectors.toList());
        List<String> placeholders = new ArrayList<>();
        matStrings.forEach(s -> placeholders.add("%s"));
        TranslationTextComponent matComponent = new TranslationTextComponent(String.join("-", placeholders), matStrings.toArray());
        return new TranslationTextComponent(this.getTranslationKey(stack), matComponent);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (ModularUtils.isNull(stack) || ModularUtils.isBroken(stack))
            return UseAction.NONE;
        return UseAction.BLOCK;
    }

    @Override
    public final int getMaxDamage(ItemStack stack) {
        if (isNull(stack))
            return 1;
        List<PartType> partList = getPartList(stack.getItem());
        return (int) (IntStream.range(0, partList.size()).filter(i -> partList.get(i) instanceof ArmorPartType).map(i -> ((ArmorPartType) partList.get(i)).getExtraDurability(getToolMaterial(stack, i), EquipmentSlotType.FEET)).sum() * IntStream.range(0, partList.size()).filter(i -> partList.get(i) instanceof ArmorPartType).mapToDouble(i -> ((ArmorPartType) partList.get(i)).getDurabilityModifier(getToolMaterial(stack, i), EquipmentSlotType.FEET)).reduce(1, (d, p) -> d * p) * getBoostMultiplier(stack) + 1);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> list) {
        if (isInGroup(group) && group != ItemGroup.SEARCH)
            Materials.getAll().stream().filter(mat -> mat.getArmorMaterial() != null).map(mat -> {
                ItemStack result = new ItemStack(this);
                CompoundNBT mainCompound = new CompoundNBT();
                mainCompound.put("Materials", new CompoundNBT());
                mainCompound.putLong("XP", 0);
                mainCompound.putInt("Level", 0);
                mainCompound.put("Modifiers", new CompoundNBT());
                mainCompound.put("Boosts", new CompoundNBT());
                mainCompound.putInt("Damage", 0);
                mainCompound.putInt("ModifierSlotsUsed", 0);
                CompoundNBT materialNBT = new CompoundNBT();
                StreamUtils.repeat(getToolParts(this).size(), i -> materialNBT.putString("material" + i, mat.getName()));
                mainCompound.put("Materials", materialNBT);
                result.setTag(mainCompound);
                return result;
            }).forEach(list::add);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (ModularUtils.isNull(stack) || ModularUtils.isBroken(stack))
            return new ActionResult<>(ActionResultType.PASS, stack);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (amount > 1 && !ModularUtils.isNull(stack) && !ModularUtils.isBroken(stack))
            ModularUtils.addXP(stack, amount / 2, entity);
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
        return true;
    }

    @Override
    public void addStats(ItemStack stack, List<ITextComponent> tooltip, boolean compact) {
        tooltip.add(new TranslationTextComponent("tool.stat.level", ModularUtils.getLevel(stack), ModularUtils.getLevelCap(stack)));
        long currentXP;
        long nextXP;
        if (ModularUtils.getLevel(stack) < ModularUtils.getLevelCap(stack)) {
            currentXP = ModularUtils.getXPForCurrentLevel(stack);
            nextXP = ModularUtils.getXPForLevelUp(stack);
        } else {
            currentXP = ModularUtils.getXPForLevel(ModularUtils.getLevel(stack) - 1);
            nextXP = ModularUtils.getXPForCurrentLevel(stack);
        }
        tooltip.add(new TranslationTextComponent("tool.stat.experience", ModularUtils.getXP(stack) - currentXP, nextXP - currentXP));
        tooltip.add(new TranslationTextComponent("tool.stat.modifier_slots", ModularUtils.getFreeModifierSlotCount(stack)));
        int maxDamage = stack.getMaxDamage() - 1;
        tooltip.add(new TranslationTextComponent("tool.stat.current_durability", maxDamage - stack.getDamage(), maxDamage));
        ModularUtils.getAllAbilities(stack).stream().collect(Collectors.toMap(a -> a, a -> 1, Integer::sum)).forEach((a, v) -> {
            if (v > 1)
                tooltip.add(new StringTextComponent(a.getTranslationKey(stack).getFormattedText() + " x" + v));
            else
                tooltip.add(a.getTranslationKey(stack));
        });
        ModularUtils.getModifiersStats(stack).forEach(s -> tooltip.add(new StringTextComponent(s.getModifier().getFormatting() + s.getModifier().getNameTextComponent(stack, s).getFormattedText())));
    }
}
