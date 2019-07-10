package com.NovumScientiaTeam.modulartoolkit.tools;

import com.EmosewaPixel.pixellib.materialSystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.abilities.Abilities;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.partTypes.PartType;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolTypeMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils.*;

public abstract class ModularTool extends Item {
    private ImmutableList<PartType> partList;
    private List<String> toolTags = new ArrayList<>();

    public ModularTool(String name, ImmutableList<PartType> partList) {
        super(new Properties().group(ModularToolkit.TOOL_GROUP).maxStackSize(1).setNoRepair());
        setRegistryName(name);
        this.partList = partList;
        addPropertyOverride(new ResourceLocation("broken"), (stack, world, entity) -> isBroken(stack) ? 1 : 0);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (isNull(stack))
            return new TranslationTextComponent(this.getTranslationKey(stack));

        List<ITextComponent> matStrings = getHeadMaterials(stack).stream().map(Material::getTranslationKey).collect(Collectors.toList());
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
                tooltip.add(new StringTextComponent(TextFormatting.RED + new TranslationTextComponent("tool.stat.broken").getString()));
            getAllAbilities(stack).forEach(a -> tooltip.add(a.getTranslationKey(stack)));
            tooltip.add(new TranslationTextComponent("tool.stat.level", getLevel(stack), getLevelCap(stack)));
            tooltip.add(new TranslationTextComponent("tool.stat.experience", getXP(stack) - getXPForCurentLevel(stack), getXPForLevelUp(stack) - getXPForCurentLevel(stack)));
            tooltip.add(new TranslationTextComponent("tool.stat.modifier_slots", getFreeModifierSlotCount(stack)));
            int maxDamage = getMaxDamage(stack) - 1;
            tooltip.add(new TranslationTextComponent("tool.stat.current_durability", maxDamage - stack.getDamage(), maxDamage));
            tooltip.add(new TranslationTextComponent("tool.stat.attack_damage", getAttackDamage(stack) + 1));
            getToolTypes(stack).forEach(t -> {
                tooltip.add(new TranslationTextComponent("tool.stat.harvest_level_" + t.getName(), new TranslationTextComponent("harvest_level_" + getHarvestMap(stack).get(t))));
                tooltip.add(new TranslationTextComponent("tool.stat.efficiency_" + t.getName(), getDestroySpeedForToolType(stack, t)));
            });
            getModifierTierMap(stack).forEach((modifier, tier) ->
                    tooltip.add(modifier.getTextComponent(stack, tier)));
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
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getAttackDamage(stack), AttributeModifier.Operation.ADDITION));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getAttackSpeed(stack), AttributeModifier.Operation.ADDITION));
        }

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
        return (int) (IntStream.range(0, partList.size()).map(i -> partList.get(i).getExtraDurability(getToolMaterial(stack, i))).sum() * IntStream.range(0, partList.size()).mapToDouble(i -> partList.get(i).getDurabilityModifier(getToolMaterial(stack, i))).reduce(1, (d, p) -> d * p) + 1);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (isNull(stack))
            return amount;
        for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
            amount = e.getKey().onToolDamaged(amount, e.getValue());
        for (AbstractAbility ability : Abilities.getAll())
            amount = ability.onToolDamaged(stack, amount);
        int max = getMaxDamage(stack) - 1 - stack.getDamage();
        if (amount >= max && !isBroken(stack))
            makeBroken(stack);
        return Math.min(amount, max);
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
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F && !isBroken(stack))
            if (hasTag(IS_TOOL)) {
                stack.damageItem(1, entityLiving, (p_220038_0_) -> {
                    p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
                addXP(stack);
            } else if (hasTag(IS_MELEE_WEAPON))
                stack.damageItem(2, entityLiving, (p_220038_0_) -> {
                    p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });

        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!isBroken(stack))
            if (hasTag(IS_MELEE_WEAPON) || hasTag(IS_HOE))
                stack.damageItem(1, attacker, (p_220039_0_) -> {
                    p_220039_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
            else if (hasTag(IS_TOOL))
                stack.damageItem(2, attacker, (p_220039_0_) -> {
                    p_220039_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });

        if (hasTag(IS_MELEE_WEAPON) && !target.isAlive())
            addXP(stack, (int) target.getMaxHealth());

        return true;
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !(player.isCreative() && hasTag(IS_MELEE_WEAPON));
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

    public abstract double getAttackDamage(ItemStack stack);

    public abstract double getAttackSpeed(ItemStack stack);
}