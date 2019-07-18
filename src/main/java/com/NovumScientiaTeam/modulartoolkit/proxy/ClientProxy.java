package com.NovumScientiaTeam.modulartoolkit.proxy;

import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.EmosewaPixel.pixellib.proxy.IModProxy;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.ObjTypeRegistry;
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
            if (((IMaterialItem) item).getMaterial().getItemTier() != null) {
                if (obj.hasTag(ObjTypeRegistry.HEAD)) {
                    tooltip.add(new TranslationTextComponent("tool_part_type.head").applyTextStyle(TextFormatting.UNDERLINE));
                    new Head().addTooltip(item, tooltip);
                }
                if (obj.hasTag(ObjTypeRegistry.HANDLE)) {
                    tooltip.add(new TranslationTextComponent("tool_part_type.handle").applyTextStyle(TextFormatting.UNDERLINE));
                    new Handle().addTooltip(item, tooltip);
                }
                if (obj.hasTag(ObjTypeRegistry.EXTRA)) {
                    tooltip.add(new TranslationTextComponent("tool_part_type.extra").applyTextStyle(TextFormatting.UNDERLINE));
                    new Extra().addTooltip(item, tooltip);
                }
            }
        }
        if (Modifiers.getFor(item) != null) {
            AbstractModifier modifier = Modifiers.getFor(item).getModifier();
            if (InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                tooltip.add(new StringTextComponent(modifier.getFormatting() + modifier.getNameTextComponent(e.getItemStack(), null).getString()));
                tooltip.add(new StringTextComponent("   -" + modifier.getFormatting() + modifier.getDescTextComponent().getString()));
                tooltip.add(new StringTextComponent("   -" + modifier.getFormatting() + new TranslationTextComponent("part.tooltip.level_cap", modifier.getLevelCap()).getString()));
            } else
                tooltip.add(new TranslationTextComponent("part.tooltip.mod_button"));
        }
    }
}