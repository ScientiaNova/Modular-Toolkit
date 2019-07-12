package com.NovumScientiaTeam.modulartoolkit.tables.blocks;

import com.EmosewaPixel.pixellib.blocks.ModBlock;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.providers.PartConstructorContainerProvider;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.PartConstructorTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class PartConstructorBlock extends ModBlock {
    public PartConstructorBlock() {
        super(Properties.from(Blocks.CRAFTING_TABLE), ModularToolkit.MOD_ID + ":part_constructor", 0);
    }

    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new PartConstructorContainerProvider(pos, this.getRegistryName()), pos);
        }

        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PartConstructorTile();
    }
}
