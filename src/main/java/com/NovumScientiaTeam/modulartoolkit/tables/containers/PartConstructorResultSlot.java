package com.NovumScientiaTeam.modulartoolkit.tables.containers;

import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.NovumScientiaTeam.modulartoolkit.ObjTypeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class PartConstructorResultSlot extends SlotItemHandler {
    public PartConstructorResultSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        Item item = stack.getItem();
        ObjectType objectType;
        Material mat;
        if (item instanceof IMaterialItem) {
            objectType = ((IMaterialItem) item).getObjType();
            mat = ((IMaterialItem) item).getMaterial();
        }
        else {
            objectType = MaterialItems.getItemObjType(stack.getItem());
            mat = MaterialItems.getItemMaterial(stack.getItem());
        }
        if (objectType != null) {
            IItemHandler handler = getItemHandler();
            ItemStack stackIn = handler.getStackInSlot(0);
            float amount = objectType.getBucketVolume() / 144f;
            if (stackIn.getItem() instanceof IMaterialItem && ((IMaterialItem) stackIn.getItem()).getObjType() == ObjTypeRegistry.FRAGMENT)
                handler.extractItem(0, (int) (amount * 2), false);
            else {
                handler.extractItem(0, (int) Math.ceil(amount), false);
                if (amount - (int) amount == 0.5f)
                    handler.insertItem(2, new ItemStack(MaterialItems.getItem(mat, ObjTypeRegistry.FRAGMENT)), false);
            }
        } else
            return null;
        return stack;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }
}
