package com.NovumScientiaTeam.modulartoolkit.modifiers;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class ModifierRegistry {
    public static void register() {
        Modifiers.addModifier(Items.QUARTZ, new SharpnessMod());
        Modifiers.addModifier(Items.REDSTONE, new HasteMod());
        Modifiers.addModifier(Items.BLAZE_POWDER, new FireyMod());
        Modifiers.addModifier(Blocks.OBSIDIAN.asItem(), new HardenedMod());
        Modifiers.addModifier(Items.GLOWSTONE, new GlowingMod());
        Modifiers.addModifier(Items.LAPIS_LAZULI, new LuckMod());
        Modifiers.addModifier(Items.ENCHANTED_GOLDEN_APPLE, new EnchantedMod());
    }
}
