package com.NovumScientiaTeam.modulartoolkit.tables.blocks;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockRegistry {
    private static List<Item> blockItems = new ArrayList<>();
    public static Block PART_CONSTRUCTOR;
    public static Block MODIFICATION_STATION;

    public static void registrBlocks(RegistryEvent.Register<Block> e) {
        PART_CONSTRUCTOR = register(new PartConstructorBlock(), e);
        MODIFICATION_STATION = register(new ModificationStationBlock(), e);
    }

    public static void registerItems(RegistryEvent.Register<Item> e) {
        blockItems.forEach(i -> e.getRegistry().register(i));
    }

    private static Block register(Block block, RegistryEvent.Register<Block> e) {
        e.getRegistry().register(block);
        blockItems.add(new BlockItem(block, new Item.Properties().group(ModularToolkit.MAIN_GROUP)) {
            @OnlyIn(Dist.CLIENT)
            public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
                tooltip.add(new TranslationTextComponent("tooltip" + getTranslationKey().substring(5)));
            }
        }.setRegistryName(block.getRegistryName()));
        return block;
    }
}
