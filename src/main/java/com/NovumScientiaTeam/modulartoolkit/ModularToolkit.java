package com.NovumScientiaTeam.modulartoolkit;

import com.EmosewaPixel.pixellib.proxy.IModProxy;
import com.NovumScientiaTeam.modulartoolkit.proxy.ClientProxy;
import com.NovumScientiaTeam.modulartoolkit.proxy.ServerProxy;
import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPatternRegistry;
import com.NovumScientiaTeam.modulartoolkit.recipes.SerializerRegistry;
import com.NovumScientiaTeam.modulartoolkit.tables.blocks.BlockRegistry;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.PartConstructorContainer;
import com.NovumScientiaTeam.modulartoolkit.tables.screens.PartConstructorScreen;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.PartConstructorTile;
import com.NovumScientiaTeam.modulartoolkit.tools.ToolRegistry;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolTypeMap;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModularToolkit.MOD_ID)
public class ModularToolkit {
    public static final String MOD_ID = "modulartoolkit";

    private IModProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static final ItemGroup MAIN_GROUP = new ItemGroup("mt_main") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ToolRegistry.PICKAXE);
        }
    };

    public static ContainerType CONSTRUCTOR_CONTAINER;
    public static TileEntityType CONSTRUCTOR;

    public ModularToolkit() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);


        MinecraftForge.EVENT_BUS.register(this);

        new ObjTypeRegistry();
        ConstructorPatternRegistry.setup();
        PacketHandler.setup();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        proxy.enque(event);
        ToolTypeMap.init();
    }

    private void processIMC(final InterModProcessEvent event) {
        proxy.process(event);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(CONSTRUCTOR_CONTAINER, PartConstructorScreen::new);
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
        public static void onTETypeRegistry(RegistryEvent.Register<TileEntityType<?>> e) {
            e.getRegistry().register(CONSTRUCTOR = TileEntityType.Builder.create(PartConstructorTile::new, BlockRegistry.PART_CONSTRUCTOR).build(null).setRegistryName(BlockRegistry.PART_CONSTRUCTOR.getRegistryName()));
        }

        @SubscribeEvent
        public static void onContainerTypeRegistry(RegistryEvent.Register<ContainerType<?>> e) {
            e.getRegistry().register(CONSTRUCTOR_CONTAINER = IForgeContainerType.create(PartConstructorContainer::new).setRegistryName(BlockRegistry.PART_CONSTRUCTOR.getRegistryName()));
        }

        @SubscribeEvent
        public static void onRecipeSerializerRegistry(RegistryEvent.Register<IRecipeSerializer<?>> e) {
            SerializerRegistry.registry(e);
        }
    }
}