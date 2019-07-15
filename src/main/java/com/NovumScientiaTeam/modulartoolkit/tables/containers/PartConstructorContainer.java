package com.NovumScientiaTeam.modulartoolkit.tables.containers;

import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialsystem.lists.Materials;
import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.EmosewaPixel.pixellib.miscutils.StreamUtils;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.ObjTypeRegistry;
import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPattern;
import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPatternRegistry;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.PartConstructorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.AirItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private void addMachineSlots() {
        this.addSlot(new SlotItemHandler(this.itemHandler, 0, 8, 35));
        this.addSlot(new PartConstructorResultSlot(this.itemHandler, 1, 130, 35));
        this.addSlot(new SlotItemHandler(this.itemHandler, 2, 152, 35) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
            }
        });
    }

    private void addPlayerSlots(IInventory playerInventory) {
        StreamUtils.repeat(3, i ->
                StreamUtils.repeat(9, j ->
                        this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))));
        StreamUtils.repeat(9, k -> this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142)));
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            itemstack = stack1.copy();
            if (index == 2)
                slot.onTake(playerIn, stack1);
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

            List<Byte> data = new ArrayList<>();
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
        te.getCurrentPattern().pattern[y][x] = (byte) data;
    }

    private void updateOutput() {
        Optional<Material> material = getMaterialOfItem(itemHandler.getStackInSlot(0));
        ObjectType objectType = getMatchingObjectType();
        ItemStack fragmentStack = itemHandler.getStackInSlot(2);
        float consumeAmount;
        if (objectType != null && material.isPresent() && MaterialItems.contains(material.get(), objectType) && getMaterialValue(itemHandler.getStackInSlot(0)) >= (consumeAmount = (objectType.getBucketVolume() / 144f)) && (fragmentStack == ItemStack.EMPTY || fragmentStack.getItem() instanceof AirItem || ((IMaterialItem) fragmentStack.getItem()).getMaterial() == material.get()) && (consumeAmount - (int) consumeAmount != 0.5f || fragmentStack.getCount() < fragmentStack.getMaxStackSize())) {
            itemHandler.setStackInSlot(1, new ItemStack(MaterialItems.getItem(material.get(), objectType)));
        } else
            itemHandler.setStackInSlot(1, ItemStack.EMPTY);
    }

    private Optional<Material> getMaterialOfItem(ItemStack stack) {
        if (stack.getItem() instanceof IMaterialItem && ((IMaterialItem) stack.getItem()).getObjType() == ObjTypeRegistry.FRAGMENT)
            return Optional.of(((IMaterialItem) stack.getItem()).getMaterial());
        return Materials.getAll().stream().filter(m -> m.getItemTier() != null && m.getItemTier().getRepairMaterial().test(stack)).findFirst();
    }

    private float getMaterialValue(ItemStack stack) {
        if (stack.getItem() instanceof IMaterialItem && ((IMaterialItem) stack.getItem()).getObjType() == ObjTypeRegistry.FRAGMENT)
            return stack.getCount() / 2f;
        return stack.getCount();
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