package com.NovumScientiaTeam.modulartoolkit.tables.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ModificationStationResultSlot extends SlotItemHandler {
    private Map<Integer, Integer> consumeMap = new HashMap<>();

    public ModificationStationResultSlot(IItemHandler handler, int index, int xPosition, int yPosition) {
        super(handler, index, xPosition, yPosition);
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
        IItemHandler handler = getItemHandler();
        handler.extractItem(0, 1, false);
        boolean isServerPlayer = player instanceof ServerPlayerEntity;
        consumeMap.forEach((k, v) -> {
            handler.extractItem(k + 1, v, false);
            if (isServerPlayer && v != 0)
                ((ServerPlayerEntity) player).connection.sendPacket(new SSetSlotPacket(((ServerPlayerEntity) player).currentWindowId, k + 1, handler.getStackInSlot(k + 1)));
        });
        return stack;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }

    protected void setConsumeMap(Map<Integer, Integer> consumeMap) {
        this.consumeMap = consumeMap;
    }
}