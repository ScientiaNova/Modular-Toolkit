package com.NovumScientiaTeam.modulartoolkit.items.tools;

import com.NovumScientiaTeam.modulartoolkit.items.util.IWrenchableBlock;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

import java.util.Optional;

public class ModularWrench extends ModularTool {
    public ModularWrench() {
        super("modulartoolkit:wrench_tool");
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return 0;
        return ModularUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 1;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return -2.5;
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if (ModularUtils.isBroken(stack) || ModularUtils.isNull(stack))
            return ActionResultType.PASS;
        World world = context.getWorld();
        BlockState state = world.getBlockState(context.getPos());
        PlayerEntity player = context.getPlayer();
        if (state.getBlock() instanceof IWrenchableBlock)
            return ((IWrenchableBlock) state.getBlock()).whenWrenched(stack, context);
        Optional<IProperty<?>> rotation = state.getBlock().getStateContainer().getProperties().stream().filter(p -> p instanceof DirectionProperty).findFirst();
        if (rotation.isPresent() && !world.isRemote)
            if (rotation.get().getAllowedValues().contains(context.getFace())) {
                if (!player.isSneaking())
                    world.setBlockState(context.getPos(), state.with((DirectionProperty) rotation.get(), context.getFace()));
                else
                    world.setBlockState(context.getPos(), state.with((DirectionProperty) rotation.get(), context.getFace().getOpposite()));
                stack.damageItem(1, player, (p_220038_0_) -> {
                    p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
                ModularUtils.addXP(stack, context.getPlayer());
                return ActionResultType.SUCCESS;
            }

        return ActionResultType.PASS;
    }
}