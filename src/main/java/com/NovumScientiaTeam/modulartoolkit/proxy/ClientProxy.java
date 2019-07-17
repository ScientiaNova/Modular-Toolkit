package com.NovumScientiaTeam.modulartoolkit.proxy;

import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.EmosewaPixel.pixellib.proxy.IModProxy;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.ObjTypeRegistry;
import com.NovumScientiaTeam.modulartoolkit.abilities.Abilities;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.modifiers.AbstractModifier;
import com.NovumScientiaTeam.modulartoolkit.modifiers.Modifiers;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Extra;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Handle;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.tools.ToolRegistry;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModularToolkit.MOD_ID, value = Dist.CLIENT)
public class ClientProxy implements IModProxy {
    @Override
    public void init() {

    }

    @Override
    public void enque(InterModEnqueueEvent interModEnqueueEvent) {
        ToolRegistry.TOOLS.forEach(t ->
                Minecraft.getInstance().getItemColors().register((stack, index) -> {
                    if (ToolUtils.getToolMaterial(stack, index) != null)
                        return ToolUtils.getToolMaterial(stack, index).getColor();
                    return -1;
                }, t));
    }

    @Override
    public void process(InterModProcessEvent interModProcessEvent) {

    }

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent e) {
        Item item = e.getItemStack().getItem();
        List<ITextComponent> tooltip = e.getToolTip();
        if (item instanceof IMaterialItem) {
            ObjectType obj = ((IMaterialItem) item).getObjType();
            Material mat = ((IMaterialItem) item).getMaterial();
            if (mat.getItemTier() != null) {
                DecimalFormat format = new DecimalFormat("#.##");
                AbstractAbility ability;
                if (obj.hasTag(ObjTypeRegistry.HEAD)) {
                    Head head = new Head();
                    tooltip.add(new TranslationTextComponent("tool_part_type.head").applyTextStyle(TextFormatting.UNDERLINE));
                    tooltip.add(new TranslationTextComponent("tool.stat.attack_damage", format.format(mat.getItemTier().getAttackDamage() + 1)));
                    if (!obj.hasTag(ObjTypeRegistry.WEAPON_PART)) {
                        tooltip.add(new TranslationTextComponent("tool.stat.harvest_level", new TranslationTextComponent("harvest_level_" + mat.getItemTier().getHarvestLevel())));
                        tooltip.add(new TranslationTextComponent("tool.stat.efficiency", format.format(mat.getItemTier().getEfficiency())));
                    }
                    tooltip.add(new TranslationTextComponent("tool.stat.durability", Integer.toString(head.getExtraDurability(mat))));
                    tooltip.add(new TranslationTextComponent("tool.stat.level_cap_multiplier", format.format(head.getLevelCapMultiplier(mat))));
                    ;
                    ability = Abilities.getFor(mat, head);
                    if (ability != null)
                        tooltip.add(ability.getTranslationKey(e.getItemStack()));
                }
                if (obj.hasTag(ObjTypeRegistry.HANDLE)) {
                    Handle handle = new Handle();
                    tooltip.add(new TranslationTextComponent("tool_part_type.handle").applyTextStyle(TextFormatting.UNDERLINE));
                    tooltip.add(new TranslationTextComponent("tool.stat.durability", Integer.toString(handle.getExtraDurability(mat))));
                    tooltip.add(new TranslationTextComponent("tool.stat.durability_multiplier", format.format(handle.getDurabilityModifier(mat))));
                    ;
                    ability = Abilities.getFor(mat, handle);
                    if (ability != null)
                        tooltip.add(ability.getTranslationKey(e.getItemStack()));
                }
                if (obj.hasTag(ObjTypeRegistry.EXTRA)) {
                    Extra extra = new Extra();
                    tooltip.add(new TranslationTextComponent("tool_part_type.extra").applyTextStyle(TextFormatting.UNDERLINE));
                    tooltip.add(new TranslationTextComponent("tool.stat.durability", Integer.toString(extra.getExtraDurability(mat))));
                    tooltip.add(new TranslationTextComponent("tool.stat.level_cap_multiplier", format.format(extra.getLevelCapMultiplier(mat))));
                    ability = Abilities.getFor(mat, extra);
                    if (ability != null)
                        tooltip.add(ability.getTranslationKey(e.getItemStack()));
                }
            }
        }
        if (Modifiers.getFor(item) != null) {
            AbstractModifier modifier = Modifiers.getFor(item);
            if (InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                tooltip.add(new StringTextComponent(modifier.getFormatting() + modifier.getNameTextComponent(e.getItemStack(), null).getString()));
                tooltip.add(new StringTextComponent("   -" + modifier.getFormatting() + modifier.getDescTextComponent().getString()));
                tooltip.add(new StringTextComponent("   -" + modifier.getFormatting() + new TranslationTextComponent("part.tooltip.level_cap", modifier.getLevelCap()).getString()));
            } else
                tooltip.add(new TranslationTextComponent("part.tooltip.mod_button"));
        }
    }
}