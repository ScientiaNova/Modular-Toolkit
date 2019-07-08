package com.NovumScientiaTeam.modulartoolkit;

import com.EmosewaPixel.pixellib.proxy.IModProxy;
import com.NovumScientiaTeam.modulartoolkit.proxy.ClientProxy;
import com.NovumScientiaTeam.modulartoolkit.proxy.ServerProxy;
import com.NovumScientiaTeam.modulartoolkit.recipes.SerializerRegistry;
import com.NovumScientiaTeam.modulartoolkit.tables.blocks.BlockRegistry;
import com.NovumScientiaTeam.modulartoolkit.tools.ToolRegistry;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolTypeMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModularToolkit.MOD_ID)
public class ModularToolkit {
    public static final String MOD_ID = "modulartoolkit";

    public static final Logger LOGGER = LogManager.getLogger();

    private IModProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static final ItemGroup TOOL_GROUP = new ItemGroup("mt_tools") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ToolRegistry.PICKAXE);
        }
    };

    public static final ItemGroup TABLE_GROUP = new ItemGroup("mt_tables") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(BlockRegistry.PART_CONSTRUCTOR);
        }
    };

    public ModularToolkit() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);

        new ObjTypeRegistry();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        proxy.enque(event);
        ToolTypeMap.init();
    }

    private void processIMC(final InterModProcessEvent event) {
        proxy.process(event);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemRegistry(RegistryEvent.Register<Item> e) {
            ToolRegistry.registry(e);
            BlockRegistry.registerItems(e);
        }

        @SubscribeEvent
        public static void onBlockRegistry(RegistryEvent.Register<Block> e) {
            BlockRegistry.registrBlocks(e);
        }

        @SubscribeEvent
        public static void onRecipeSerializerRegistry(RegistryEvent.Register<IRecipeSerializer<?>> e) {
            SerializerRegistry.registry(e);
        }
    }
}