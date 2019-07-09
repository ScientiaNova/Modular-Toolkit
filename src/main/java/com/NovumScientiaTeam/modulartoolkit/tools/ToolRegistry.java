package com.NovumScientiaTeam.modulartoolkit.tools;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class ToolRegistry {
    public static final List<Item> TOOLS = new ArrayList<>();

    public static Item AXE;
    public static Item HOE;
    public static Item PAXEL;
    public static Item PICKAXE;
    public static Item SHOVEL;
    public static Item SWORD;
    public static Item WRENCH;

    public static void registry(RegistryEvent.Register<Item> e) {
        AXE = register(new AxeTool(), e);
        HOE = register(new HoeTool(), e);
        PAXEL = register(new PaxelTool(), e);
        PICKAXE = register(new PickaxeTool(), e);
        SHOVEL = register(new ShovelTool(), e);
        SWORD = register(new SwordTool(), e);
        WRENCH = register(new WrenchTool(), e);
    }

    private static Item register(Item i, RegistryEvent.Register<Item> e) {
        e.getRegistry().register(i);
        TOOLS.add(i);
        return i;
    }
}