package com.NovumScientiaTeam.modulartoolkit.items.util;

import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.PartType;
import net.minecraft.block.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils.*;

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

    public static boolean breakBlock(ItemStack stack, World world, BlockState state, BlockPos pos, ServerPlayerEntity player) {
        if (!stack.getItem().canPlayerBreakBlockWhileHolding(state, world, pos, player)) {
            return false;
        } else {
            TileEntity tileentity = world.getTileEntity(pos);
            Block block = state.getBlock();
            if ((block instanceof CommandBlockBlock || block instanceof StructureBlock || block instanceof JigsawBlock) && !player.canUseCommandBlock()) {
                world.notifyBlockUpdate(pos, state, state, 3);
                return false;
            } else if (player.func_223729_a(world, pos, player.interactionManager.getGameType()))
                return false;
            else {
                block.onBlockHarvested(world, pos, state, player);
                boolean flag = world.removeBlock(pos, false);
                if (flag) {
                    block.onPlayerDestroy(world, pos, state);
                    for (Map.Entry<AbstractModifier, Integer> e : getModifierTierMap(stack).entrySet())
                        e.getKey().onBlockDestroyed(stack, world, state, pos, player, e.getValue());
                    for (AbstractAbility ability : getAllAbilities(stack))
                        ability.onBlockDestroyed(stack, world, state, pos, player);
                }
                if (player.isCreative()) {
                    return true;
                } else {
                    boolean flag1 = player.canHarvestBlock(state);
                    if (flag && flag1) {
                        block.harvestBlock(world, player, pos, state, tileentity, stack);
                        if (state.getBlockHardness(world, pos) != 0.0F && !isBroken(stack)) {
                            stack.damageItem(1, player, e -> {
                            });
                            addXP(stack, player);
                        }
                    }

                    return true;
                }
            }
        }
    }
}