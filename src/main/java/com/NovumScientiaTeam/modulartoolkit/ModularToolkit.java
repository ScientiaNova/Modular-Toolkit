package com.NovumScientiaTeam.modulartoolkit;

import com.EmosewaPixel.pixellib.materialSystem.MaterialRegistry;
import com.EmosewaPixel.pixellib.materialSystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialSystem.materials.IngotMaterial;
import com.EmosewaPixel.pixellib.materialSystem.types.ItemType;
import com.EmosewaPixel.pixellib.materialSystem.types.ObjectType;
import com.EmosewaPixel.pixellib.proxy.IModProxy;
import com.NovumScientiaTeam.modulartoolkit.proxy.ClientProxy;
import com.NovumScientiaTeam.modulartoolkit.proxy.ServerProxy;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("modulartoolkit")
public class ModularToolkit {
    private IModProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static ObjectType STICK;
    public static ObjectType LONG_STICK;
    public static ObjectType AXE_HEAD;
    public static ObjectType HOE_HEAD;
    public static ObjectType PICKAXE_HEAD;
    public static ObjectType SWORD_HEAD;

    public ModularToolkit() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);

        STICK = new ItemType("stick", m -> m instanceof IngotMaterial && !m.hasTag(MaterialRegistry.BLOCK_FROM_4X4)).setBucketVolume(72).build();
        LONG_STICK = new ItemType("long_stick", m -> (m instanceof IngotMaterial && !m.hasTag(MaterialRegistry.BLOCK_FROM_4X4)) || m == MaterialRegistry.WOODEN).setBucketVolume(144).build();
        AXE_HEAD = new ItemType("axe_head", m -> m.getItemTier() != null).setBucketVolume(432).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        HOE_HEAD = new ItemType("hoe_head", m -> m.getItemTier() != null).setBucketVolume(288).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        PICKAXE_HEAD = new ItemType("pickaxe_head", m -> m.getItemTier() != null).setBucketVolume(432).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();
        SWORD_HEAD = new ItemType("sword_head", m -> m.getItemTier() != null).setBucketVolume(288).addTypeTag(MaterialRegistry.SINGLE_TEXTURE_TYPE).build();

        MaterialItems.addItem(MaterialRegistry.WOODEN, STICK, Items.STICK);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        proxy.enque(event);
    }

    private void processIMC(final InterModProcessEvent event) {
        proxy.process(event);
    }
}
