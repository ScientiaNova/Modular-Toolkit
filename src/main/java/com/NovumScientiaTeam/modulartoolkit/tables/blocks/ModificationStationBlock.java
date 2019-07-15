package com.NovumScientiaTeam.modulartoolkit.tables.blocks;

import com.EmosewaPixel.pixellib.blocks.ModBlock;
import com.EmosewaPixel.pixellib.miscutils.ItemUtils;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.providers.ModificationStationContainerProvider;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.providers.PartConstructorContainerProvider;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.ModificationStationTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class ModificationStationBlock extends ModBlock {
    public ModificationStationBlock() {
        super(Properties.from(Blocks.CRAFTING_TABLE), ModularToolkit.MOD_ID + ":modification_station", 0);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ModificationStationTile();
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof ModificationStationTile) {
            ItemStackHandler handler = ((ModificationStationTile) te).getItemStackHandler();
            ItemUtils.spawnItemInWorld(world, pos, handler.getStackInSlot(0));
            ItemUtils.spawnItemInWorld(world, pos, handler.getStackInSlot(1));
        }
        super.harvestBlock(world, player, pos, state, te, stack);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new ModificationStationContainerProvider(pos, this.getRegistryName()), pos);
        }

        return true;
    }
}