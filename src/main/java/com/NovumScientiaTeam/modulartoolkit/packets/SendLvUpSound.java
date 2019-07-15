package com.NovumScientiaTeam.modulartoolkit.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvents;

public class SendLvUpSound {
    public SendLvUpSound() {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F));
    }
}