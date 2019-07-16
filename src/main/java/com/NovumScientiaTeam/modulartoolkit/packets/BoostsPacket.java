package com.NovumScientiaTeam.modulartoolkit.packets;

import com.NovumScientiaTeam.modulartoolkit.tables.tiles.ModificationStationTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BoostsPacket {
    private BlockPos pos;
    private int boosts;

    public BoostsPacket(BlockPos pos, int boosts) {
        this.pos = pos;
        this.boosts = boosts;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(boosts);
    }

    public static BoostsPacket decode(PacketBuffer buffer) {
        return new BoostsPacket(buffer.readBlockPos(), buffer.readInt());
    }

    public static void processPacket(BoostsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            ModificationStationTile tile = (ModificationStationTile) player.world.getTileEntity(msg.pos);
            tile.setBoosts(msg.boosts);
            tile.markDirty();
        });

        ctx.get().setPacketHandled(true);
    }
}
