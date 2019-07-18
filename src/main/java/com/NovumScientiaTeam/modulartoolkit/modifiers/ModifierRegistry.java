package com.NovumScientiaTeam.modulartoolkit.modifiers;

import com.NovumScientiaTeam.modulartoolkit.modifiers.util.ModifierStack;
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
        Modifiers.giveModifier(Items.QUARTZ, new ModifierStack(SHARPNESS));
        Modifiers.giveModifier(Blocks.QUARTZ_BLOCK.asItem(), new ModifierStack(SHARPNESS, 4));
        Modifiers.giveModifier(Items.REDSTONE, new ModifierStack(HASTE));
        Modifiers.giveModifier(Blocks.REDSTONE_BLOCK.asItem(), new ModifierStack(HASTE, 9));
        Modifiers.giveModifier(Items.BLAZE_POWDER, new ModifierStack(FIREY));
        Modifiers.giveModifier(Blocks.OBSIDIAN.asItem(), new ModifierStack(HARDENED));
        Modifiers.giveModifier(Items.GLOWSTONE_DUST, new ModifierStack(GLOWING));
        Modifiers.giveModifier(Blocks.GLOWSTONE.asItem(), new ModifierStack(GLOWING, 4));
        Modifiers.giveModifier(Items.LAPIS_LAZULI, new ModifierStack(LUCK));
        Modifiers.giveModifier(Blocks.LAPIS_BLOCK.asItem(), new ModifierStack(LUCK, 9));
        Modifiers.giveModifier(Items.ENCHANTED_GOLDEN_APPLE, new ModifierStack(ENCHANTED));
        Modifiers.giveModifier(Items.TOTEM_OF_UNDYING, new ModifierStack(REVIVING));
    }
}
