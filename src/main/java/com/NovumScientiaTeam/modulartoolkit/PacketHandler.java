package com.NovumScientiaTeam.modulartoolkit;

import com.NovumScientiaTeam.modulartoolkit.tables.containers.PacketPattern;
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
        INSTANCE.registerMessage(patternSetID, PacketPattern.class, PacketPattern::encode, PacketPattern::decode, PacketPattern::processPacket);
    }

    public static final int patternSetID = 0;
}
