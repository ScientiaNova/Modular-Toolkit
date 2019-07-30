package com.NovumScientiaTeam.modulartoolkit.items.tools;

import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import com.NovumScientiaTeam.modulartoolkit.items.util.ToolUtils;
import javafx.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class ModularBroadaxe extends ModularTool {
    public ModularBroadaxe() {
        super("modulartoolkit:broadaxe_tool");
        addToolTags(ModularUtils.IS_TOOL);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return 0;
        return ModularUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 8;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return -3.4F;
        return -3.8F + (ModularUtils.getToolMaterial(stack, 0).getItemTier().getEfficiency() / 2 - 1) * 0.1F;
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public int getAOERange() {
        return 2;
    }

    @Override
    public List<BlockPos> getAOEPoses(ItemStack stack, World world, BlockState state, BlockPos pos, ServerPlayerEntity player, boolean harvest) {
        MinecraftForge.EVENT_BUS.register(new TreeFelling(stack, world, pos, player));
        return Collections.EMPTY_LIST;
    }

    public static class TreeFelling {
        private Queue<BlockPos> posQueue = new LinkedList<>();
        private Set<BlockPos> usedPoses = new HashSet<>();

        private ItemStack stack;
        private World world;
        private ServerPlayerEntity player;
        BlockPos current;
        BlockState currentState;
        private BlockPos initial;

        public TreeFelling(ItemStack stack, World world, BlockPos start, ServerPlayerEntity player) {
            this.stack = stack;
            this.world = world;
            this.initial = start;
            posQueue.add(start);
            this.player = player;
        }

        @SubscribeEvent
        public void onTick(TickEvent.WorldTickEvent e) {
            if (e.side.isClient()) {
                MinecraftForge.EVENT_BUS.unregister(this);
                return;
            }

            if (e.world.getDimension() != world.getDimension()) {
                return;
            }

            int left = 10;

            while (left > 0) {
                if (ModularUtils.isBroken(stack) || posQueue.isEmpty()) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                    return;
                }

                current = posQueue.remove();
                currentState = world.getBlockState(current);
                if (!usedPoses.add(current))
                    continue;

                for (Pair<Integer, Integer> pair : new Pair[]{new Pair<>(1, 1), new Pair<>(1, 0), new Pair<>(0, 1), new Pair<>(1, -1), new Pair<>(-1, 1), new Pair<>(-1, 0), new Pair<>(0, -1), new Pair<>(-1, -1)}) {
                    BlockPos temp = current.add(pair.getKey(), 0, pair.getValue());
                    if (BlockTags.LOGS.contains(world.getBlockState(temp).getBlock()) && !usedPoses.contains(temp))
                        posQueue.add(temp);

                }

                if (current != initial && !BlockTags.LOGS.contains(currentState.getBlock()))
                    continue;

                if (!usedPoses.contains(current.up()))
                    posQueue.add(current.up());

                if (current != initial)
                    ToolUtils.breakBlock(stack, world, currentState, current, player);
                left--;
            }
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (ModularUtils.isBroken(stack) || ModularUtils.isNull(stack))
            return ActionResultType.PASS;
        ActionResultType result = Items.DIAMOND_AXE.onItemUse(context);
        if (result == ActionResultType.SUCCESS)
            ModularUtils.addXP(stack, context.getPlayer());
        return result;
    }
}