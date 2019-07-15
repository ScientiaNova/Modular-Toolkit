package com.NovumScientiaTeam.modulartoolkit.tables.tiles;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class ModificationStationTile extends TileEntity {
    private ItemStackHandler itemStackHandler;

    public ModificationStationTile() {
        super(ModularToolkit.STATION);
        itemStackHandler = new ItemStackHandler(6);
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT compoundNBT = super.write(compound);
        compoundNBT.put("handler", itemStackHandler.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("handler"))
            itemStackHandler.deserializeNBT(compound.getCompound("handler"));
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }
}