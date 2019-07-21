package com.NovumScientiaTeam.modulartoolkit.tables.containers;

import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.EmosewaPixel.pixellib.miscutils.StreamUtils;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;
import com.NovumScientiaTeam.modulartoolkit.modifiers.Modifiers;
import com.NovumScientiaTeam.modulartoolkit.modifiers.util.ModifierStack;
import com.NovumScientiaTeam.modulartoolkit.modifiers.util.ModifierStats;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.ModificationStationTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils.*;

public class ModificationStationContainer extends Container {
    protected ModificationStationTile te;
    protected ItemStackHandler itemHandler;

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
                return stack.getItem() instanceof ModularItem;
            }
        });
        this.addSlot(new SlotItemHandler(this.itemHandler, 1, 23, 35) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return !(stack.getItem() instanceof ModularItem);
            }
        });
        this.addSlot(new SlotItemHandler(this.itemHandler, 2, 44, 14) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return !(stack.getItem() instanceof ModularItem);
            }
        });
        this.addSlot(new SlotItemHandler(this.itemHandler, 3, 65, 35) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return !(stack.getItem() instanceof ModularItem);
            }
        });
        this.addSlot(new SlotItemHandler(this.itemHandler, 4, 44, 56) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return !(stack.getItem() instanceof ModularItem);
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
        listeners.forEach(l -> l.sendWindowProperty(this, 0, te.getBoosts()));
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0)
            te.setBoosts(data);
    }

    private void updateOutput() {
        ItemStack inputTool = itemHandler.getStackInSlot(0);
        if (inputTool.isEmpty()) {
            itemHandler.setStackInSlot(5, ItemStack.EMPTY);
            te.setBoosts(0);
            return;
        }

        ItemStack outputTool = inputTool.copy();

        List<Material> toolMats = getAllToolMaterials(inputTool);
        List<ObjectType> toolParts = getToolParts(inputTool.getItem());

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
                repairItem(outputTool, (int) Math.ceil(consumeCount * singleRepairAmount));
                consumeMap.put(i, consumeCount);
            }
        });

        //Replacing
        StreamUtils.repeat(4, i -> {
            if (!consumeMap.containsKey(i) && !outputTool.isDamaged() && modStacks.get(i).getItem() instanceof IMaterialItem) {
                final IMaterialItem current = (IMaterialItem) modStacks.get(i).getItem();
                List<Material> toolMatsNew = getAllToolMaterials(outputTool);
                List<Integer> indices = IntStream.range(0, toolParts.size()).filter(j -> toolMatsNew.get(j) != current.getMaterial() && toolParts.get(j) == current.getObjType()).boxed().collect(Collectors.toList());
                if (!indices.isEmpty()) {
                    setToolMaterial(outputTool, indices.get(i % indices.size()), current.getMaterial());
                    consumeMap.put(i, 1);
                }
            }
        });

        //Balancing new level
        final int levelCap = getLevelCap(outputTool);
        if (getLevelCap(inputTool) > levelCap && getXPForLevel(levelCap) < getXP(outputTool)) {
            setXP(outputTool, getXPForLevel(levelCap));
            setLevel(outputTool, levelCap);
            List<ModifierStats> modStats = getModifiersStats(outputTool);
            if (modStats.stream().anyMatch(s -> s.getAdded() > levelCap || s.getTier() > levelCap)) {
                modStats.forEach(s -> s.getModifier().whenRemoved(outputTool, s.getTier()));
                remapModifiers(outputTool, getModifiersStats(outputTool).stream().filter(stats -> stats.getAdded() <= levelCap).map(stats -> {
                    if (stats.getTier() > levelCap) {
                        stats.setTier(levelCap);
                        stats.setConsumed(stats.getModifier().getLevelRequirement(levelCap));
                    }
                    return stats.serialize();
                }).collect(Collectors.toList()));
            }
            if (getBoosts(outputTool).stream().anyMatch(b -> b > levelCap))
                remapBoosts(outputTool, getBoosts(outputTool).stream().filter(b -> b <= levelCap).collect(Collectors.toList()));
            if (getUsedModifierSlotCount(outputTool) > levelCap)
                setUsedModifierSlotCount(outputTool, levelCap);
        }

        //Adding Modifiers
        StreamUtils.repeat(4, i -> {
            if (!consumeMap.containsKey(i) && Modifiers.getFor(modStacks.get(i).getItem()) != null) {
                LinkedHashMap<AbstractModifier, Integer> currentModifiers = getModifierTierMap(outputTool);
                final ItemStack current = modStacks.get(i);
                final ModifierStack stack = Modifiers.getFor(current.getItem());
                final AbstractModifier modifier = stack.getModifier();
                final int count = stack.getCount();
                final LinkedList<Map.Entry<AbstractModifier, Integer>> linkedList = new LinkedList<>(currentModifiers.entrySet());
                if (currentModifiers.keySet().contains(modifier)) {
                    int modifierIndex = IntStream.range(0, linkedList.size()).filter(j -> linkedList.get(j).getKey().equals(modifier)).findFirst().getAsInt();
                    CompoundNBT modifierNBT = getModifierNBT(outputTool, modifierIndex);
                    int alreadyAdded = modifierNBT.getInt("consumed");
                    final int initialTier = linkedList.get(modifierIndex).getValue();
                    int tier = initialTier;
                    int consumed = 0;
                    while (modifier.canLevelUp(outputTool, tier + 1)) {
                        double neededForLevelUp = (modifier.getLevelRequirement(tier + 1) - alreadyAdded) / (double) count;
                        if (Math.ceil(neededForLevelUp) <= current.getCount()) {
                            if (neededForLevelUp != Math.ceil(neededForLevelUp) && !modifier.canLevelUp(outputTool, tier + 2)) {
                                consumed = (int) neededForLevelUp;
                                break;
                            } else {
                                consumed = (int) Math.ceil(neededForLevelUp);
                                tier++;
                            }
                        } else {
                            consumed = current.getCount();
                            break;
                        }
                    }
                    modifierNBT.putInt("consumed", alreadyAdded + consumed * count);
                    modifierNBT.putInt("tier", tier);
                    if (tier > initialTier)
                        modifier.whenGainedLevel(outputTool, tier);
                    consumeMap.put(i, consumed);
                } else if (getFreeModifierSlotCount(outputTool) > 0 && modifier.canBeAdded(outputTool)) {
                    int tier = 0;
                    int consumed = 0;
                    while (modifier.canLevelUp(outputTool, tier + 1)) {
                        double neededForLevelUp = modifier.getLevelRequirement(tier + 1) / (double) count;
                        if (Math.ceil(neededForLevelUp) <= current.getCount()) {
                            if (neededForLevelUp != Math.ceil(neededForLevelUp) && !modifier.canLevelUp(outputTool, tier + 2)) {
                                consumed = (int) neededForLevelUp;
                                break;
                            } else {
                                consumed = (int) Math.ceil(neededForLevelUp);
                                tier++;
                            }
                        } else {
                            consumed = current.getCount();
                            break;
                        }
                    }
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("name", modifier.getName());
                    nbt.putInt("consumed", consumed * count);
                    nbt.putInt("tier", tier);
                    if (tier > 0)
                        modifier.whenGainedLevel(outputTool, tier);
                    nbt.putInt("added", getUsedModifierSlotCount(outputTool) + 1);
                    useModifierSlot(outputTool);
                    outputTool.getTag().getCompound("Modifiers").put("modifier" + currentModifiers.entrySet().size(), nbt);
                    consumeMap.put(i, consumed);
                }
            }
        });

        //Boosts
        int modifierSlots = getFreeModifierSlotCount(outputTool);
        if (te.getBoosts() > modifierSlots)
            te.setBoosts(modifierSlots);
        if (te.getBoosts() > 0 && !isBroken(outputTool)) {
            int boosts = te.getBoosts();
            int currentBoostCount = getBoosts(outputTool).size();
            int usedModifierSlot = getLevel(outputTool) - modifierSlots;
            CompoundNBT boostsNBT = outputTool.getTag().getCompound("Boosts");
            for (int i = 0; i < boosts; i++)
                boostsNBT.putInt("boost" + currentBoostCount++, ++usedModifierSlot);
            useModifierSlots(outputTool, boosts);
        }

        updateConsumeMap(consumeMap);
        itemHandler.setStackInSlot(5, outputTool);
    }

    public ModificationStationTile getTe() {
        return te;
    }

    private void updateConsumeMap(Map<Integer, Integer> consumeMap) {
        ((ModificationStationResultSlot) getSlot(5)).setConsumeMap(consumeMap);
    }
}