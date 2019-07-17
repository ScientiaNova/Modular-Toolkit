package com.NovumScientiaTeam.modulartoolkit.modifiers;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class ModifierRegistry {
    public static final SharpnessMod SHARPNESS = new SharpnessMod();
    public static final HasteMod HASTE = new HasteMod();
    public static final FireyMod FIREY = new FireyMod();
    public static final HardenedMod HARDENED = new HardenedMod();
    public static final GlowingMod GLOWING = new GlowingMod();
    public static final LuckMod LUCK = new LuckMod();
    public static final EnchantedMod ENCHANTED = new EnchantedMod();
    public static final RevivingMod REVIVING = new RevivingMod();

    public static void register() {
        Modifiers.addModifier(Items.QUARTZ, SHARPNESS);
        Modifiers.addModifier(Items.REDSTONE, HASTE);
        Modifiers.addModifier(Items.BLAZE_POWDER, FIREY);
        Modifiers.addModifier(Blocks.OBSIDIAN.asItem(), HARDENED);
        Modifiers.addModifier(Items.GLOWSTONE, GLOWING);
        Modifiers.addModifier(Items.LAPIS_LAZULI, LUCK);
        Modifiers.addModifier(Items.ENCHANTED_GOLDEN_APPLE, ENCHANTED);
        Modifiers.addModifier(Items.TOTEM_OF_UNDYING, REVIVING);
    }
}
