package com.NovumScientiaTeam.modulartoolkit.tools;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.partTypes.PartType;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolTypeMap;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ModularTool extends Item {
    private ImmutableList<PartType> partList;
    private List<String> toolTags = new ArrayList<>();

    public ModularTool(String name, ImmutableList<PartType> partList) {
        super(new Properties().group(ModularToolkit.GROUP).maxStackSize(1));
        setRegistryName(name);
        this.partList = partList;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return new TranslationTextComponent(this.getTranslationKey(stack));

        Object[] matStrings = partList.stream().filter(t -> t instanceof Head).mapToInt(t -> partList.indexOf(t)).mapToObj(i -> ToolUtils.getToolMaterial(stack, i).getTranslationKey()).toArray();
        List<String> placeholders = new ArrayList<>();
        Arrays.asList(matStrings).forEach(s -> placeholders.add("%s"));
        TranslationTextComponent matComponent = new TranslationTextComponent(String.join("-", placeholders), matStrings);
        return new TranslationTextComponent(this.getTranslationKey(stack), matComponent);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!ToolUtils.isNull(stack)) {
            tooltip.add(new TranslationTextComponent("tool.stat.level", ToolUtils.getLevel(stack), ToolUtils.getLevelCap(stack)));
            tooltip.add(new TranslationTextComponent("tool.stat.experience", ToolUtils.getXP(stack) - ToolUtils.getXPForCurentLevel(stack), ToolUtils.getXPForLevelUp(stack)));
            tooltip.add(new TranslationTextComponent("tool.stat.attack_damage", getAttackDamage(stack)));
            getToolTypes(stack).forEach(t -> {
                tooltip.add(new TranslationTextComponent("tool.stat.harvest_level_" + t.getName(), new TranslationTextComponent("harvest_level_" + ToolUtils.getHarvestMap(stack).get(t))));
                tooltip.add(new TranslationTextComponent("tool.stat.efficiency_" + t.getName(), ToolUtils.getDestroySpeedForToolType(stack, t)));
            });
        }
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        return partList.stream().filter(p -> p instanceof Head).map(h -> ((Head) h).getToolType()).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        if (ToolUtils.isNull(stack))
            return -1;
        if (ToolUtils.isBroken(stack))
            return -1;
        return ToolUtils.getHarvestMap(stack).getOrDefault(tool, -1);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EquipmentSlotType.MAINHAND && !ToolUtils.isBroken(stack)) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getAttackDamage(stack), AttributeModifier.Operation.ADDITION));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getAttackSpeed(stack), AttributeModifier.Operation.ADDITION));
        }

        return multimap;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (ToolUtils.isNull(stack))
            return 1;
        if (ToolUtils.isBroken(stack))
            return 1;
        if (ToolTypeMap.contains(state.getBlock()))
            return ToolUtils.getDestroySpeedForToolType(stack, ToolTypeMap.get(state.getBlock()));
        return (float) getToolTypes(stack).stream().filter(state::isToolEffective).mapToDouble(t -> ToolUtils.getDestroySpeedForToolType(stack, t)).max().orElse(1);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public final int getMaxDamage(ItemStack stack) {
        if (ToolUtils.isNull(stack))
            return 1;
        return (int) (IntStream.range(0, partList.size()).map(i -> partList.get(i).getExtraDurability(ToolUtils.getToolMaterial(stack, i))).sum() * IntStream.range(0, partList.size()).mapToDouble(i -> partList.get(i).getDurabilityModifier(ToolUtils.getToolMaterial(stack, i))).reduce(1, (d, p) -> d * p));
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        int max = getMaxDamage(stack) - 1;
        super.setDamage(stack, Math.min(damage, max));

        if (damage >= max)
            ToolUtils.makeBroken(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return super.showDurabilityBar(stack) && !ToolUtils.isBroken(stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F)
            if (hasTag(ToolUtils.IS_TOOL))
                stack.damageItem(1, entityLiving, (p_220038_0_) -> {
                    p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
            else if (hasTag(ToolUtils.IS_MELEE_WEAPON))
                stack.damageItem(2, entityLiving, (p_220038_0_) -> {
                    p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });

        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (hasTag(ToolUtils.IS_MELEE_WEAPON))
            stack.damageItem(1, attacker, (p_220039_0_) -> {
                p_220039_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
        else if (hasTag(ToolUtils.IS_TOOL))
            stack.damageItem(2, attacker, (p_220039_0_) -> {
                p_220039_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });

        return true;
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