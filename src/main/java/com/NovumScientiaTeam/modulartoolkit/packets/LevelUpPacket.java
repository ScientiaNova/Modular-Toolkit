package com.NovumScientiaTeam.modulartoolkit.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
            Runnable run = DistExecutor.runForDist(() -> () -> LevelUpPacket::playSound, () -> () -> () -> {
            });
            run.run();
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void playSound() {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F));
    }
}
