package com.NovumScientiaTeam.modulartoolkit.tools;

import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialsystem.lists.Materials;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.EmosewaPixel.pixellib.miscutils.StreamUtils;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.partTypes.PartType;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolTypeMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils.*;

public abstract class ModularTool extends Item {
    private ImmutableList<PartType> partList;
    private List<String> toolTags = new ArrayList<>();

    public ModularTool(String name, ImmutableList<PartType> partList) {
        super(new Properties().group(ModularToolkit.MAIN_GROUP).maxStackSize(1).setNoRepair());
        setRegistryName(name);
        this.partList = partList;
        addPropertyOverride(new ResourceLocation("broken"), (stack, world, entity) -> isBroken(stack) ? 1 : 0);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (isNull(stack))
            return new TranslationTextComponent(this.getTranslationKey(stack));

        List<ITextComponent> matStrings = getHeadMaterials(stack).stream().map(Material::getTranslationKey).distinct().collect(Collectors.toList());
        List<String> placeholders = new ArrayList<>();
        matStrings.forEach(s -> placeholders.add("%s"));
        TranslationTextComponent matComponent = new TranslationTextComponent(String.join("-", placeholders), matStrings.toArray());
        return new TranslationTextComponent(this.getTranslationKey(stack), matComponent);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!isNull(stack)) {
            if (isBroken(stack))
                tooltip.add(new TranslationTextComponent("tool.stat.broken").applyTextStyle(TextFormatting.RED));
            if (InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                DecimalFormat format = new DecimalFormat("#.#");
                tooltip.add(new TranslationTextComponent("tool.stat.level", getLevel(stack), getLevelCap(stack)));
                long currentXP;
                long nextXP;
                if (getLevel(stack) < getLevelCap(stack)) {
                    currentXP = getXPForCurrentLevel(stack);
                    nextXP = getXPForLevelUp(stack);
                } else {
                    currentXP = getXPForLevel(getLevel(stack) - 1);
                    nextXP = getXPForCurrentLevel(stack);
                }
                tooltip.add(new TranslationTextComponent("tool.stat.experience", getXP(stack) - currentXP, nextXP - currentXP));
                tooltip.add(new TranslationTextComponent("tool.stat.modifier_slots", getFreeModifierSlotCount(stack)));
                int maxDamage = getMaxDamage(stack) - 1;
                tooltip.add(new TranslationTextComponent("tool.stat.current_durability", maxDamage - stack.getDamage(), maxDamage));
                tooltip.add(new TranslationTextComponent("tool.stat.attack_damage", format.format(getTrueAttackDamage(stack) + 1)));
                getToolTypes(stack).forEach(t -> {
                    tooltip.add(new TranslationTextComponent("tool.stat.harvest_level_" + t.getName(), new TranslationTextComponent("harvest_level_" + getHarvestMap(stack).get(t))));
                    tooltip.add(new TranslationTextComponent("tool.stat.efficiency_" + t.getName(), format.format(getDestroySpeedForToolType(stack, t))));
                });
                getAllAbilities(stack).stream().collect(Collectors.toMap(a -> a, a -> 1, Integer::sum)).forEach((a, v) -> {
                    if (v > 1)
                        tooltip.add(new StringTextComponent(v + "x " + a.getTranslationKey(stack).getFormattedText()));
                    else
                        tooltip.add(a.getTranslationKey(stack));
                });
                getModifiersStats(stack).forEach(s -> tooltip.add(new StringTextComponent(s.getModifier().getFormatting() + s.getModifier().getNameTextComponent(stack, s).getFormattedText())));
            } else if (InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
                List<Material> materials = getAllToolMaterials(stack);
                List<ObjectType> parts = getToolParts(stack.getItem());
                IntStream.range(0, materials.size())
                        .mapToObj(i -> MaterialItems.getItem(materials.get(i), parts.get(i)))
                        .forEach(item -> {
                            List<ITextComponent> list = new ItemStack(item).getTooltip(Minecraft.getInstance().player, flagIn);
                            list.set(0, new StringTextComponent(TextFormatting.BOLD + list.get(0).getFormattedText()));
                            tooltip.addAll(list);
                        });
            } else {
                tooltip.add(new TranslationTextComponent("tool.tooltip.tool_button"));
                tooltip.add(new TranslationTextComponent("tool.tooltip.part_button"));
            }
        }
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        return partList.stream().filter(p -> p instanceof Head).map(h -> ((Head) h).getToolType()).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        if (isNull(stack) || isBroken(stack))
            return -1;
        return getHarvestMap(stack).getOrDefault(tool, -1);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EquipmentSlotType.MAINHAND && !isBroken(stack)) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getTrueAttackDamage(stack), AttributeModifier.Operation.ADDITION));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getTrueAttackSpeed(stack), AttributeModifier.Operation.ADDITION));
        }

        for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
            multimap = e.getKey().getAttributeModifiers(slot, stack, e.getValue(), multimap);
        for (AbstractAbility ability : getAllAbilities(stack))
            multimap = ability.getAttributeModifiers(slot, stack, multimap);

        return multimap;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isNull(stack) || isBroken(stack))
            return 1;
        if (ToolTypeMap.contains(state.getBlock()))
            return getDestroySpeedForToolType(stack, ToolTypeMap.get(state.getBlock()));
        return (float) getToolTypes(stack).stream().filter(state::isToolEffective).mapToDouble(t -> getDestroySpeedForToolType(stack, t)).max().orElse(1);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public final int getMaxDamage(ItemStack stack) {
        if (isNull(stack))
            return 1;
        return (int) (IntStream.range(0, partList.size()).map(i -> partList.get(i).getExtraDurability(getToolMaterial(stack, i))).sum() * IntStream.range(0, partList.size()).mapToDouble(i -> partList.get(i).getDurabilityModifier(getToolMaterial(stack, i))).reduce(1, (d, p) -> d * p) * getBoostMultiplier(stack) + 1);
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
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity livingEntity) {
        for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
            e.getKey().onBlockDestroyed(stack, worldIn, state, pos, livingEntity, e.getValue());
        for (AbstractAbility ability : getAllAbilities(stack))
            ability.onBlockDestroyed(stack, worldIn, state, pos, livingEntity);

        if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F && !isBroken(stack))
            if (hasTag(IS_TOOL)) {
                stack.damageItem(1, livingEntity, (p_220038_0_) ->
                        p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND)
                );
                addXP(stack, livingEntity);
            } else if (hasTag(IS_MELEE_WEAPON))
                stack.damageItem(2, livingEntity, (p_220038_0_) ->
                        p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND)
                );

        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
            e.getKey().onHitEntity(stack, target, attacker, e.getValue());
        for (AbstractAbility ability : getAllAbilities(stack))
            ability.onHitEntity(stack, target, attacker);

        if (!isBroken(stack))
            if (hasTag(IS_MELEE_WEAPON) || hasTag(IS_HOE))
                stack.damageItem(1, attacker, (p_220039_0_) ->
                        p_220039_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND)
                );
            else if (hasTag(IS_TOOL))
                stack.damageItem(2, attacker, (p_220039_0_) ->
                        p_220039_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND)
                );

        if (hasTag(IS_MELEE_WEAPON) && !target.isAlive())
            addXP(stack, (int) target.getMaxHealth(), attacker);

        return true;
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !(player.isCreative() && hasTag(IS_MELEE_WEAPON));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
            e.getKey().onInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected, e.getValue());
        for (AbstractAbility ability : getAllAbilities(stack))
            ability.onInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> list) {
        if (isInGroup(group))
            Materials.getAll().stream().filter(mat -> mat.getItemTier() != null).map(mat -> {
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
                StreamUtils.repeat(partList.size(), i -> materialNBT.putString("material" + i, mat.getName()));
                mainCompound.put("Materials", materialNBT);
                result.setTag(mainCompound);
                return result;
            }).forEach(list::add);
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

    public ImmutableList<PartType> getPartList() {
        return partList;
    }

    public double getTrueAttackDamage(ItemStack stack) {
        double amount = getAttackDamage(stack);
        for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
            amount = e.getKey().setAttackDamage(stack, e.getValue(), amount);
        for (AbstractAbility ability : getAllAbilities(stack))
            amount = ability.setAttackDamage(stack, amount);
        return amount * getBoostMultiplier(stack);
    }

    public double getTrueAttackSpeed(ItemStack stack) {
        double amount = getAttackSpeed(stack);
        for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
            amount = e.getKey().setAttackSpeed(stack, e.getValue(), amount);
        for (AbstractAbility ability : getAllAbilities(stack))
            amount = ability.setAttackSpeed(stack, amount);
        return amount;
    }

    public abstract double getAttackDamage(ItemStack stack);

    public abstract double getAttackSpeed(ItemStack stack);
}