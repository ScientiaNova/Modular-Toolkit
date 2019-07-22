package com.NovumScientiaTeam.modulartoolkit.parts.partTypes;

import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.NovumScientiaTeam.modulartoolkit.abilities.Abilities;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.parts.ObjTypeRegistry;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.modifications.PartModification;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class Head extends ToolPartType {
    private ToolType toolType;

    public Head(ToolType toolType) {
        super("head");
        this.toolType = toolType;
    }

    public Head() {
        this(null);
    }

    @Override
    public int getExtraDurability(Material mat) {
        return (int) (mat.getItemTier().getMaxUses() * 0.8 * mods.stream().mapToDouble(PartModification::getExtraDurabilityMultiplier).reduce(1, (d1, d2) -> d1 * d2));
    }

    @Override
    public double getDurabilityModifier(Material mat) {
        return 1;
    }

    @Override
    public double getLevelCapMultiplier(Material mat) {
        return mat.getItemTier().getEnchantability() * mat.getItemTier().getEfficiency() / 10;
    }

    public Optional<ToolType> getToolType() {
        return Optional.ofNullable(toolType);
    }

    @Override
    public void addTooltip(Item item, List<ITextComponent> tooltip) {
        DecimalFormat format = new DecimalFormat("#.##");
        Material mat = ((IMaterialItem) item).getMaterial();
        ObjectType obj = ((IMaterialItem) item).getObjType();
        tooltip.add(new TranslationTextComponent("tool.stat.attack_damage", format.format(mat.getItemTier().getAttackDamage() + 1)));
        if (!obj.hasTag(ObjTypeRegistry.WEAPON_PART)) {
            tooltip.add(new TranslationTextComponent("tool.stat.harvest_level", new TranslationTextComponent("harvest_level_" + mat.getItemTier().getHarvestLevel())));
            tooltip.add(new TranslationTextComponent("tool.stat.efficiency", format.format(mat.getItemTier().getEfficiency())));
        }
        tooltip.add(new TranslationTextComponent("tool.stat.durability", Integer.toString(getExtraDurability(mat))));
        tooltip.add(new TranslationTextComponent("tool.stat.level_cap_multiplier", format.format(getLevelCapMultiplier(mat))));
        AbstractAbility ability = Abilities.getFor(mat, this);
        if (ability != null)
            tooltip.add(ability.getTranslationKey(new ItemStack(item)));
    }
}
