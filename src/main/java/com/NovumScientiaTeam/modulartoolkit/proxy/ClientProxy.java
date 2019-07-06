package com.NovumScientiaTeam.modulartoolkit.proxy;

import com.EmosewaPixel.pixellib.materialSystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialSystem.materials.Material;
import com.EmosewaPixel.pixellib.materialSystem.types.ObjectType;
import com.EmosewaPixel.pixellib.proxy.IModProxy;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.ObjTypeRegistry;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Extra;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Handle;
import com.NovumScientiaTeam.modulartoolkit.partTypes.Head;
import com.NovumScientiaTeam.modulartoolkit.tools.ToolRegistry;
import net.minecraft.client.Minecraft;
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

import java.text.DecimalFormat;

@Mod.EventBusSubscriber(modid = ModularToolkit.MOD_ID, value = Dist.CLIENT)
public class ClientProxy implements IModProxy {
    @Override
    public void init() {

    }

    @Override
    public void enque(InterModEnqueueEvent interModEnqueueEvent) {
        ToolRegistry.TOOLS.forEach(t->
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
        if (item instanceof IMaterialItem) {
            ObjectType obj = ((IMaterialItem) item).getObjType();
            Material mat = ((IMaterialItem) item).getMaterial();
            if (mat.getItemTier() != null) {
                DecimalFormat format = new DecimalFormat("#.##");
                if (obj.hasTag(ObjTypeRegistry.HEAD)) {
                    Head head = new Head(null);
                    e.getToolTip().add(underline(new TranslationTextComponent("tool_part_type.head")));
                    e.getToolTip().add(new TranslationTextComponent("tool.stat.attack_damage", format.format(mat.getItemTier().getAttackDamage() + 1)));
                    if (!obj.hasTag(ObjTypeRegistry.WEAPON_PART)) {
                        e.getToolTip().add(new TranslationTextComponent("tool.stat.harvest_level", new TranslationTextComponent("harvest_level_" + mat.getItemTier().getHarvestLevel())));
                        e.getToolTip().add(new TranslationTextComponent("tool.stat.efficiency", format.format(mat.getItemTier().getEfficiency())));
                    }
                    e.getToolTip().add(new TranslationTextComponent("tool.stat.durability", Integer.toString(head.getExtraDurability(mat))));
                    e.getToolTip().add(new TranslationTextComponent("tool.stat.level_cap_multiplier", format.format(head.getLevelCapMultiplier(mat))));
                }
                if (obj.hasTag(ObjTypeRegistry.HANDLE)) {
                    Handle handle = new Handle();
                    e.getToolTip().add(underline(new TranslationTextComponent("tool_part_type.handle")));
                    e.getToolTip().add(new TranslationTextComponent("tool.stat.durability", Integer.toString(handle.getExtraDurability(mat))));
                    e.getToolTip().add(new TranslationTextComponent("tool.stat.durability_multiplier", format.format(handle.getDurabilityModifier(mat))));
                }
                if (obj.hasTag(ObjTypeRegistry.EXTRA)) {
                    e.getToolTip().add(underline(new TranslationTextComponent("tool_part_type.extra")));
                    e.getToolTip().add(new TranslationTextComponent("tool.stat.durability", Integer.toString(new Extra().getExtraDurability(mat))));
                    e.getToolTip().add(new TranslationTextComponent("tool.stat.level_cap_multiplier", format.format(new Extra().getLevelCapMultiplier(mat))));
                }
            }
        }
    }

    private static StringTextComponent underline(ITextComponent component) {
        return new StringTextComponent(TextFormatting.UNDERLINE + component.getString());
    }
}