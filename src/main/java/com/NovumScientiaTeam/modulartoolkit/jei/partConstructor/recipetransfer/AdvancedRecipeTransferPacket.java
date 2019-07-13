package com.NovumScientiaTeam.modulartoolkit.jei.partConstructor.recipetransfer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AdvancedRecipeTransferPacket {
    private int matchingSlots[];
    private int amount;
    private boolean max;

    public AdvancedRecipeTransferPacket(int matchingSlots[], int amount, boolean max) {
        this.matchingSlots = matchingSlots;
        this.amount = amount;
        this.max = max;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeVarIntArray(matchingSlots);
        buffer.writeInt(amount);
        buffer.writeBoolean(max);
    }

    public static AdvancedRecipeTransferPacket decode(PacketBuffer buffer) {
        return new AdvancedRecipeTransferPacket(buffer.readVarIntArray(), buffer.readInt(), buffer.readBoolean());
    }

    public static void processPacket(AdvancedRecipeTransferPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            Container container = player.openContainer;
            int amount = msg.amount;
            List<Slot> matchingSlots = Arrays.stream(msg.matchingSlots).mapToObj(container::getSlot).collect(Collectors.toList());
            Slot input = container.getSlot(0);
            ItemStack transfer = matchingSlots.get(0).getStack().copy();
            int transferCount = msg.max ? Math.min(matchingSlots.stream().mapToInt(s -> s.getStack().getCount()).sum(), 64) / amount * amount : amount;
            transfer.setCount(transferCount);
            for (Slot slot : matchingSlots) {
                int subtract = Math.min(slot.getStack().getCount(), transferCount);
                slot.decrStackSize(subtract);
                transferCount -= subtract;
                if (transferCount == 0)
                    break;
            }
            input.putStack(transfer);
        });

        ctx.get().setPacketHandled(true);
    }
}
