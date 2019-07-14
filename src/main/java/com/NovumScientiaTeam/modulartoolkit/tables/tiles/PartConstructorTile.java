package com.NovumScientiaTeam.modulartoolkit.tables.tiles;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class PartConstructorTile extends TileEntity {

    private ConstructorPattern currentPattern;
    private ItemStackHandler itemStackHandler;

    public PartConstructorTile() {
        super(ModularToolkit.CONSTRUCTOR);
        itemStackHandler = new ItemStackHandler(3);
        currentPattern = new ConstructorPattern(new byte[7][7]);
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT compoundNBT = super.write(compound);
        compoundNBT.put("handler", itemStackHandler.serializeNBT());
        compoundNBT.put("pattern", currentPattern.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("handler"))
            itemStackHandler.deserializeNBT(compound.getCompound("handler"));
        if (compound.contains("pattern"))
            currentPattern.deserializeNBT(compound.getCompound("pattern"));
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public ConstructorPattern getCurrentPattern() {
        return currentPattern;
    }

    public void setCurrentPattern(ConstructorPattern currentPattern) {
        this.currentPattern = currentPattern;
    }
}