package com.NovumScientiaTeam.modulartoolkit.tables.containers;

import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialsystem.lists.Materials;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.EmosewaPixel.pixellib.miscutils.StreamUtils;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPattern;
import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPatternRegistry;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.PartConstructorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class PartConstructorContainer extends Container {

    protected PartConstructorTile te;
    protected ItemStackHandler itemHandler;


    public PartConstructorContainer(int id, PlayerInventory playerInventory, PacketBuffer extraData) {
        this(id, playerInventory, (PartConstructorTile) playerInventory.player.world.getTileEntity(extraData.readBlockPos()));
    }


    public PartConstructorContainer(int id, PlayerInventory playerInventory, PartConstructorTile te) {
        super(ModularToolkit.CONSTRUCTOR_CONTAINER, id);
        this.te = te;
        if (te != null) {
            this.itemHandler = te.getItemStackHandler();
            this.addMachineSlots();
            this.addPlayerSlots(playerInventory);
            updateOutput();
        }
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.te.canInteractWith(playerIn);
    }

    protected void addMachineSlots() {
        this.addSlot(new SlotItemHandler(this.itemHandler, 0, 26, 35));
        this.addSlot(new PartConstructorResultSlot(this.itemHandler, 1, 134, 35));
    }

    private void addPlayerSlots(IInventory playerInventory) {
        StreamUtils.repeat(3, (i) -> {
            StreamUtils.repeat(9, (j) -> {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            });
        });
        StreamUtils.repeat(9, (k) -> {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        });
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            itemstack = stack1.copy();
            if (index == 1) {
                slot.onTake(playerIn, stack1);
                if (!this.mergeItemStack(stack1, itemHandler.getSlots(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (index < itemHandler.getSlots()) {
                if (!this.mergeItemStack(stack1, itemHandler.getSlots(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack1, 0, itemHandler.getSlots(), false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        updateOutput();
        this.listeners.forEach((listener) -> {

            List<Integer> data = new ArrayList<>();
            for (int y = 0; y < te.getCurrentPattern().pattern.length; y++) {
                for (int x = 0; x < te.getCurrentPattern().pattern[0].length; x++) {
                    data.add(te.getCurrentPattern().pattern[y][x]);
                }
            }

            for (int i = 0; i < data.size(); i++) {
                listener.sendWindowProperty(this, i, data.get(i));

            }
        });
    }

    @Override
    public void updateProgressBar(int id, int data) {
        int y = id / 7;
        int x = id - y * 7;
        te.getCurrentPattern().pattern[y][x] = data;
    }

    private void updateOutput() {
        Material material = getMaterialOfItem(itemHandler.getStackInSlot(0));
        ObjectType objectType = getMatchingObjectType();
        if (objectType != null && material != null && MaterialItems.contains(material, objectType) && itemHandler.getStackInSlot(0).getCount() >= objectType.getBucketVolume() / 144) {
            itemHandler.setStackInSlot(1, new ItemStack(MaterialItems.getItem(material, objectType)));
        } else
            itemHandler.setStackInSlot(1, ItemStack.EMPTY);
    }

    private Material getMaterialOfItem(ItemStack stack) {
        for (Material material : Materials.getAll()) {
            if (material.getItemTier() != null && material.getItemTier().getRepairMaterial().test(stack))
                return material;
        }
        return null;
    }

    private ObjectType getMatchingObjectType() {
        for (ConstructorPattern pattern : ConstructorPatternRegistry.getPossiblePatterns()) {
            if (te.getCurrentPattern().patternMatches(pattern))
                return ConstructorPatternRegistry.getObjectType(pattern);
        }
        return null;
    }

    public PartConstructorTile getTe() {
        return te;
    }
}
