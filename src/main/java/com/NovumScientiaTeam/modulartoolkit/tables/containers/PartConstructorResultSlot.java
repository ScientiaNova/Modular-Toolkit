package com.NovumScientiaTeam.modulartoolkit.tables.containers;

import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class PartConstructorResultSlot extends SlotItemHandler {
    public PartConstructorResultSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        ObjectType objectType = MaterialItems.getItemObjType(stack.getItem());
        if (objectType != null) {
            getItemHandler().extractItem(0, objectType.getBucketVolume() / 144, false);
        }
        else
            return null;
        return stack;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }
}
