package com.NovumScientiaTeam.modulartoolkit.tables.containers;

import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPattern;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.PartConstructorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PatternPacket {
    private BlockPos pos;
    private ConstructorPattern pattern;

    public PatternPacket(BlockPos pos, ConstructorPattern pattern) {
        this.pos = pos;
        this.pattern = pattern;
    }

    public PatternPacket(BlockPos pos, CompoundNBT patternNBT) {
        this.pos = pos;
        this.pattern = new ConstructorPattern();
        pattern.deserializeNBT(patternNBT);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeCompoundTag(pattern.serializeNBT());
    }

    public static PatternPacket decode(PacketBuffer buffer) {
        return new PatternPacket(buffer.readBlockPos(), buffer.readCompoundTag());
    }

    public static void processPacket(PatternPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            PartConstructorTile tile = (PartConstructorTile) player.world.getTileEntity(msg.pos);
            tile.setCurrentPattern(msg.pattern);
            tile.markDirty();
        });

        ctx.get().setPacketHandled(true);
    }
}
