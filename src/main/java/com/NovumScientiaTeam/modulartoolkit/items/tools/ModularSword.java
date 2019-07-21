package com.NovumScientiaTeam.modulartoolkit.items.tools;

import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;

public class ModularSword extends ModularTool {
    public ModularSword() {
        super("modulartoolkit:sword_tool");
        addToolTags(ModularUtils.IS_MELEE_WEAPON);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        if (ModularUtils.isNull(stack))
            return 0;
        return ModularUtils.getToolMaterial(stack, 0).getItemTier().getAttackDamage() + 3;
    }

    @Override
    public double getAttackSpeed(ItemStack stack) {
        return -2.4f;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.COBWEB)
            return 15.0F;
        Material material = state.getMaterial();
        return material != Material.PLANTS && material != Material.TALL_PLANTS && material != Material.CORAL && !state.isIn(BlockTags.LEAVES) && material != Material.GOURD ? 1.0F : 1.5F;
    }

    @Override
    public boolean canHarvestBlock(BlockState blockIn) {
        return blockIn.getBlock() == Blocks.COBWEB;
    }
}