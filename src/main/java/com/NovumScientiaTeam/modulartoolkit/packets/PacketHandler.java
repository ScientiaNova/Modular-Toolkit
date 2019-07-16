package com.NovumScientiaTeam.modulartoolkit.packets;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.jei.partConstructor.recipetransfer.AdvancedRecipeTransferPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ModularToolkit.MOD_ID, "main"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void setup() {
        INSTANCE.registerMessage(patternSetID, PatternPacket.class, PatternPacket::encode, PatternPacket::decode, PatternPacket::processPacket);
        INSTANCE.registerMessage(advancedRecipeTransfer, AdvancedRecipeTransferPacket.class, AdvancedRecipeTransferPacket::encode, AdvancedRecipeTransferPacket::decode, AdvancedRecipeTransferPacket::processPacket);
        INSTANCE.registerMessage(levelUpPacket, LevelUpPacket.class, LevelUpPacket::encode, LevelUpPacket::decode, LevelUpPacket::processPacket);
        INSTANCE.registerMessage(boostsPacket, BoostsPacket.class, BoostsPacket::encode, BoostsPacket::decode, BoostsPacket::processPacket);
    }

    public static final int patternSetID = 0;
    public static final int advancedRecipeTransfer = 1;
    public static final int levelUpPacket = 2;
    public static final int boostsPacket = 3;
}