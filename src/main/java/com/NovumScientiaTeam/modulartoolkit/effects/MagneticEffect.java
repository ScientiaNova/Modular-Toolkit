package com.NovumScientiaTeam.modulartoolkit.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MagneticEffect extends BaseEffect {
    public MagneticEffect() {
        super("modulartoolkit:magnetic", EffectType.BENEFICIAL, 0xffffff, false);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 2 == 0;
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        BlockPos entityPos = entity.getPosition();
        entity.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(entity.posX - 2, entity.posY - 2, entity.posZ - 2, entity.posX + 2, entity.posY + 2, entity.posZ + 2))
                .forEach(item -> {
                    if (!item.isAlive() || item.getItem().isEmpty())
                        return;

                    Vec3d vec = new Vec3d(entityPos);
                    vec.subtract(item.posX, item.posY, item.posZ);
                    vec.normalize();
                    vec.scale(0.07f);
                    item.getMotion().add(vec);
                });
    }
}
