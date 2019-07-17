package com.NovumScientiaTeam.modulartoolkit.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

public class BaseEffect extends Effect {
    private boolean visible;

    public BaseEffect(String name, EffectType typeIn, int liquidColorIn, boolean visible) {
        super(typeIn, liquidColorIn);
        setRegistryName(name);
        this.visible = visible;
    }

    @Override
    public boolean shouldRender(EffectInstance effect) {
        return visible;
    }

    @Override
    public boolean shouldRenderInvText(EffectInstance effect) {
        return visible;
    }

    @Override
    public boolean shouldRenderHUD(EffectInstance effect) {
        return visible;
    }
}
