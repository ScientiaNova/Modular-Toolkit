package com.NovumScientiaTeam.modulartoolkit.tools;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ToolRegistry {
    public static Item PICKAXE;

    public static void registry(RegistryEvent.Register<Item> e) {
        PICKAXE = register(new PickaxeTool(), e);
    }

    private static Item register(Item i, RegistryEvent.Register<Item> e) {
        e.getRegistry().register(i);
        return i;
    }
}
