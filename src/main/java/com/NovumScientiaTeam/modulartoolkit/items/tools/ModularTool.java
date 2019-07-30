package com.NovumScientiaTeam.modulartoolkit.items.tools;

import com.EmosewaPixel.pixellib.materialsystem.lists.Materials;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.miscutils.StreamUtils;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.items.util.HarvestMaterialMap;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import com.NovumScientiaTeam.modulartoolkit.items.util.ToolUtils;
import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.PartType;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.ToolPartType;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils.*;

public abstract class ModularTool extends ModularItem {
    private List<String> toolTags = new ArrayList<>();

    public ModularTool(String name) {
        super(name);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (isNull(stack))
            return new TranslationTextComponent(this.getTranslationKey(stack));

        List<ITextComponent> matStrings = ToolUtils.getHeadMaterials(stack).stream().map(Material::getTranslationKey).distinct().collect(Collectors.toList());
        List<String> placeholders = new ArrayList<>();
        matStrings.forEach(s -> placeholders.add("%s"));
        TranslationTextComponent matComponent = new TranslationTextComponent(String.join("-", placeholders), matStrings.toArray());
        return new TranslationTextComponent(this.getTranslationKey(stack), matComponent);
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        return getPartList(stack.getItem()).stream().filter(p -> p instanceof Head).map(h -> ((Head) h).getToolType()).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        if (isNull(stack) || isBroken(stack))
            return -1;
        return ToolUtils.getHarvestMap(stack).getOrDefault(tool, -1);
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
        if (HarvestMaterialMap.contains(state.getMaterial()) && getToolTypes(stack).contains(HarvestMaterialMap.get(state.getMaterial())))
            return ToolUtils.getDestroySpeedForToolType(stack, HarvestMaterialMap.get(state.getMaterial()));
        return (float) getToolTypes(stack).stream().filter(t -> state.isToolEffective(t) && getHarvestLevel(stack, t, null, state) >= state.getHarvestLevel()).mapToDouble(t -> ToolUtils.getDestroySpeedForToolType(stack, t)).max().orElse(1);
    }

    public List<BlockPos> getAOEPoses(ItemStack stack, World world, BlockState state, BlockPos pos, ServerPlayerEntity player, boolean harvest) {
        List<BlockPos> poses = new ArrayList<>();
        final float hardness = state.getPlayerRelativeBlockHardness(player, world, pos);
        final int range = getAOERange();
        if (hardness > 0) {
            Vec3d vec3d = player.getPositionVector().add(0, player.getEyeHeight(), 0);
            Vec3d vec3d1 = player.getLookVec();
            Vec3d vec3d2 = vec3d.add(vec3d1.x * 5, vec3d1.y * 5, vec3d1.z * 5);
            BlockRayTraceResult rayTrace = world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.SOURCE_ONLY, player));
            BlockPos currentPos;
            float currentHardness;
            BlockState currentState;
            if (rayTrace.getType() == RayTraceResult.Type.BLOCK)
                switch (rayTrace.getFace()) {
                    case NORTH:
                    case SOUTH:
                        for (int i = range - 1; i > -range; i--)
                            for (int j = range - 1; j > -range; j--)
                                if (!ModularUtils.isBroken(stack)) {
                                    if (i == 0 && j == 0)
                                        continue;
                                    currentPos = pos.add(j, i, 0);
                                    currentState = world.getBlockState(currentPos);
                                    currentHardness = currentState.getPlayerRelativeBlockHardness(player, world, currentPos);
                                    if (!currentState.isAir(world, currentPos) && currentHardness >= hardness && (!harvest || ToolUtils.breakBlock(stack, world, currentState, currentPos, player)))
                                        poses.add(currentPos);
                                }
                        break;
                    case EAST:
                    case WEST:
                        for (int i = range - 1; i > -range; i--)
                            for (int j = range - 1; j > -range; j--)
                                if (!ModularUtils.isBroken(stack)) {
                                    if (i == 0 && j == 0)
                                        continue;
                                    currentPos = pos.add(0, i, j);
                                    currentState = world.getBlockState(currentPos);
                                    currentHardness = currentState.getPlayerRelativeBlockHardness(player, world, currentPos);
                                    if (!currentState.isAir(world, currentPos) && currentHardness >= hardness && (!harvest || ToolUtils.breakBlock(stack, world, currentState, currentPos, player)))
                                        poses.add(currentPos);
                                }
                        break;
                    case UP:
                    case DOWN:
                        for (int i = range - 1; i > -range; i--)
                            for (int j = range - 1; j > -range; j--)
                                if (!ModularUtils.isBroken(stack)) {
                                    if (i == 0 && j == 0)
                                        continue;
                                    currentPos = pos.add(i, 0, j);
                                    currentState = world.getBlockState(currentPos);
                                    currentHardness = currentState.getPlayerRelativeBlockHardness(player, world, currentPos);
                                    if (!currentState.isAir(world, currentPos) && currentHardness >= hardness && (!harvest || ToolUtils.breakBlock(stack, world, currentState, currentPos, player)))
                                        poses.add(currentPos);
                                }
                        break;
                }
        }
        return poses;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!ModularUtils.isNull(stack) && !ModularUtils.isBroken(stack) && entityLiving instanceof PlayerEntity && stack.canHarvestBlock(state) && !worldIn.isRemote && getAOERange() > 1 && !entityLiving.isSneaking())
            getAOEPoses(stack, worldIn, state, pos, (ServerPlayerEntity) entityLiving, true);

        for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
            e.getKey().onBlockDestroyed(stack, worldIn, state, pos, entityLiving, e.getValue());
        for (AbstractAbility ability : getAllAbilities(stack))
            ability.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);

        if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F && !isBroken(stack))
            if (hasTag(IS_TOOL)) {
                stack.damageItem(1, entityLiving, e -> {
                });
                addXP(stack, entityLiving);
            } else if (hasTag(IS_MELEE_WEAPON))
                stack.damageItem(2, entityLiving, e -> {
                });

        return true;
    }

    @Override
    public final int getMaxDamage(ItemStack stack) {
        if (isNull(stack))
            return 1;
        List<PartType> partList = getPartList(stack.getItem());
        return (int) (IntStream.range(0, partList.size()).filter(i -> partList.get(i) instanceof ToolPartType).map(i -> ((ToolPartType) partList.get(i)).getExtraDurability(getToolMaterial(stack, i))).sum() * IntStream.range(0, partList.size()).filter(i -> partList.get(i) instanceof ToolPartType).mapToDouble(i -> ((ToolPartType) partList.get(i)).getDurabilityModifier(getToolMaterial(stack, i))).reduce(1, (d, p) -> d * p) * getBoostMultiplier(stack) + 1);
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
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> list) {
        if (isInGroup(group) && group != ItemGroup.SEARCH)
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
                StreamUtils.repeat(getToolParts(this).size(), i -> materialNBT.putString("material" + i, mat.getName()));
                mainCompound.put("Materials", materialNBT);
                result.setTag(mainCompound);
                return result;
            }).forEach(list::add);
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !(player.isCreative() && hasTag(IS_MELEE_WEAPON));
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState blockIn) {
        return getDestroySpeed(stack, blockIn) > 1;
    }

    public void addToolTags(String... tags) {
        toolTags.addAll(Arrays.asList(tags));
    }

    public boolean hasTag(String tag) {
        return toolTags.contains(tag);
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

    public int getAOERange() {
        return 1;
    }

    public abstract double getAttackDamage(ItemStack stack);

    public abstract double getAttackSpeed(ItemStack stack);

    @Override
    public void addStats(ItemStack stack, List<ITextComponent> tooltip, boolean compact) {
        DecimalFormat format = new DecimalFormat("#.#");
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
        tooltip.add(new TranslationTextComponent("tool.stat.attack_damage", format.format((getTrueAttackDamage(stack) + 1))));
        Set<ToolType> toolTypes = stack.getToolTypes();
        if (compact)
            toolTypes.forEach(t -> tooltip.add(new TranslationTextComponent("tool.stat." + t.getName(), new TranslationTextComponent("harvest_level_" + ToolUtils.getHarvestMap(stack).get(t)), format.format(ToolUtils.getDestroySpeedForToolType(stack, t)))));
        else
            toolTypes.forEach(t -> {
                tooltip.add(new TranslationTextComponent("tool.stat.harvest_level_" + t.getName(), new TranslationTextComponent("harvest_level_" + ToolUtils.getHarvestMap(stack).get(t))));
                tooltip.add(new TranslationTextComponent("tool.stat.efficiency_" + t.getName(), format.format(ToolUtils.getDestroySpeedForToolType(stack, t))));
            });
        ModularUtils.getAllAbilities(stack).stream().collect(Collectors.toMap(a -> a, a -> 1, Integer::sum)).forEach((a, v) -> {
            if (v > 1)
                tooltip.add(new StringTextComponent(a.getTranslationKey(stack).getFormattedText() + " x" + v));
            else
                tooltip.add(a.getTranslationKey(stack));
        });
        ModularUtils.getModifiersStats(stack).forEach(s -> tooltip.add(new StringTextComponent(s.getModifier().getFormatting() + s.getModifier().getNameTextComponent(stack, s).getFormattedText())));
    }
}