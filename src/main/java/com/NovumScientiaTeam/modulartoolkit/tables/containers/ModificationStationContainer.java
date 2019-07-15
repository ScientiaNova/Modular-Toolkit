package com.NovumScientiaTeam.modulartoolkit.tables.containers;

import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.EmosewaPixel.pixellib.miscutils.StreamUtils;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.ModificationStationTile;
import com.NovumScientiaTeam.modulartoolkit.tools.ModularTool;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModificationStationContainer extends Container {
    protected ModificationStationTile te;
    protected ItemStackHandler itemHandler;
    private int consumeCount;

    public ModificationStationContainer(int id, PlayerInventory playerInventory, PacketBuffer extraData) {
        this(id, playerInventory, (ModificationStationTile) playerInventory.player.world.getTileEntity(extraData.readBlockPos()));
    }

    public ModificationStationContainer(int id, PlayerInventory playerInventory, ModificationStationTile te) {
        super(ModularToolkit.STATION_CONTAINER, id);
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
        this.addSlot(new SlotItemHandler(this.itemHandler, 0, 44, 35) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.getItem() instanceof ModularTool;
            }
        });
        this.addSlot(new SlotItemHandler(this.itemHandler, 1, 23, 35) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return !(stack.getItem() instanceof ModularTool);
            }
        });
        this.addSlot(new SlotItemHandler(this.itemHandler, 2, 44, 14) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return !(stack.getItem() instanceof ModularTool);
            }
        });
        this.addSlot(new SlotItemHandler(this.itemHandler, 3, 65, 35) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return !(stack.getItem() instanceof ModularTool);
            }
        });
        this.addSlot(new SlotItemHandler(this.itemHandler, 4, 44, 56) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return !(stack.getItem() instanceof ModularTool);
            }
        });
        this.addSlot(new ModificationStationResultSlot(itemHandler, 5, 134, 35));
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
            if (index == 5)
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
    }

    private void updateOutput() {
        ItemStack inputTool = itemHandler.getStackInSlot(0);
        if (inputTool.isEmpty()) {
            itemHandler.setStackInSlot(5, ItemStack.EMPTY);
            return;
        }

        ItemStack outputTool = inputTool.copy();

        List<Material> toolMats = ToolUtils.getAllToolMaterials(inputTool);
        List<ObjectType> toolParts = ToolUtils.getToolParts(inputTool.getItem());

        List<ItemStack> modStacks = IntStream.range(1, 5).mapToObj(itemHandler::getStackInSlot).collect(Collectors.toList());
        Map<Integer, Integer> consumeMap = new HashMap<>();

        for (int i = 0; i < 4; i++)
            if (modStacks.get(i).isEmpty())
                consumeMap.put(i, 0);

        //Repairing
        StreamUtils.repeat(4, i -> {
            if (!consumeMap.containsKey(i) && outputTool.isDamaged() && toolMats.stream().anyMatch(m -> m.getItemTier().getRepairMaterial().test(modStacks.get(i)))) {
                float singleRepairAmount = outputTool.getMaxDamage() / 4f;
                int consumeCount = Math.min((int) Math.ceil(outputTool.getDamage() / singleRepairAmount), modStacks.get(i).getCount());
                ToolUtils.repairTool(outputTool, (int) Math.ceil(consumeCount * singleRepairAmount));
                consumeMap.put(i, consumeCount);
            }
        });

        //Replacing
        StreamUtils.repeat(4, i -> {
            if (!consumeMap.containsKey(i) && !outputTool.isDamaged() && modStacks.get(i).getItem() instanceof IMaterialItem) {
                final IMaterialItem current = (IMaterialItem) modStacks.get(i).getItem();
                List<Material> toolMatsNew = ToolUtils.getAllToolMaterials(outputTool);
                List<Integer> indices = IntStream.range(0, toolParts.size()).filter(j -> toolMatsNew.get(j) != current.getMaterial() && toolParts.get(j) == current.getObjType()).boxed().collect(Collectors.toList());
                if (!indices.isEmpty()) {
                    ToolUtils.setToolMaterial(outputTool, indices.get(i % indices.size()), current.getMaterial());
                    consumeMap.put(i, 1);
                }
            }
        });

        //Balancing new level
        final int levelCap = ToolUtils.getLevelCap(outputTool);
        if (ToolUtils.getLevelCap(inputTool) > levelCap && ToolUtils.getXPForLevel(levelCap) < ToolUtils.getXP(outputTool)) {
            ToolUtils.setXP(outputTool, ToolUtils.getXPForLevel(levelCap));
            ToolUtils.setLevel(outputTool, levelCap);
            if (ToolUtils.getModifierTierMap(outputTool).values().stream().anyMatch(v -> v < levelCap))
                ToolUtils.remapModifiers(outputTool, ToolUtils.getModifiersNBT(outputTool).stream().filter(nbt -> nbt.getInt("added") <= levelCap).map(nbt -> {
                    if (nbt.getInt("tier") > levelCap)
                        nbt.putInt("tier", levelCap);
                    return nbt;
                }).collect(Collectors.toList()));
            if (ToolUtils.getLevelsOfBoosts(outputTool).stream().anyMatch(b -> b > levelCap))
                ToolUtils.remapBoosts(outputTool, ToolUtils.getBoostsNBT(outputTool).stream().filter(nbt -> nbt.getInt("added") <= levelCap).collect(Collectors.toList()));
            if (ToolUtils.getUsedModifierSlotCount(outputTool) > levelCap)
                ToolUtils.setUsedModifierSlotCount(outputTool, levelCap);
        }

        //Adding Modifiers

        updateConsumeMap(consumeMap);
        itemHandler.setStackInSlot(5, outputTool);
    }

    private void consumeInputs() {
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, consumeCount, false);
    }

    public ModificationStationTile getTe() {
        return te;
    }

    private void updateConsumeMap(Map<Integer, Integer> consumeMap) {
        ((ModificationStationResultSlot) getSlot(5)).setConsumeMap(consumeMap);
    }
}