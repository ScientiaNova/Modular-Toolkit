package com.NovumScientiaTeam.modulartoolkit.parts.partTypes;

import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialsystem.materials.Material;
import com.NovumScientiaTeam.modulartoolkit.abilities.Abilities;
import com.NovumScientiaTeam.modulartoolkit.abilities.AbstractAbility;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.modifications.PartModification;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;
import java.util.List;

public class Extra extends PartType {
    public Extra() {
        super("extra");
    }

    @Override
    public int getExtraDurability(Material mat) {
        return (int) (mat.getItemTier().getMaxUses() / 5d * mods.stream().mapToDouble(PartModification::getExtraDurabilityMultiplier).reduce(1, (d1, d2) -> d1 * d2));
    }

    @Override
    public double getDurabilityModifier(Material mat) {
        return 1;
    }

    @Override
    public double getLevelCapMultiplier(Material mat) {
        return mat.getItemTier().getEnchantability() / 10d;
    }

    @Override
    public void addTooltip(Item item, List<ITextComponent> tooltip) {
        DecimalFormat format = new DecimalFormat("#.##");
        Material mat = ((IMaterialItem) item).getMaterial();
        tooltip.add(new TranslationTextComponent("tool.stat.durability", Integer.toString(getExtraDurability(mat))));
        tooltip.add(new TranslationTextComponent("tool.stat.level_cap_multiplier", format.format(getLevelCapMultiplier(mat))));
        AbstractAbility ability = Abilities.getFor(mat, this);
        if (ability != null)
            tooltip.add(ability.getTranslationKey(new ItemStack(item)));
    }
}
