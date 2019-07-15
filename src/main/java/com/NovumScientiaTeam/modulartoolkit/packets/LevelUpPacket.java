package com.NovumScientiaTeam.modulartoolkit.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class LevelUpPacket {
    public void encode(PacketBuffer buffer) {

    }

    public static LevelUpPacket decode(PacketBuffer buffer) {
        return new LevelUpPacket();
    }

    public static void processPacket(LevelUpPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Runnable run = DistExecutor.runForDist(() -> () -> SendLvUpSound::new, () -> () -> () -> {});
            run.run();
        });

        ctx.get().setPacketHandled(true);
    }


}
