package me.pixeldots.scriptedmodels.platform.other;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class LivingEntityExtras implements IExtras {

    public LivingEntity entity;
    public Level world;

    public LivingEntityExtras set(LivingEntity _entity, Level _world) {
        this.entity = _entity; this.world = _world;
        return this;
    }

    @Override public Object getMatrix() { return null; }
    @Override public VertexConsumer getVertexConsumer() { return null; }
    @Override public Matrix3f getMatrixNormal() { return null; }
    @Override public Matrix4f getMatrixPosition() { return null; }
    @Override public int getOverlay() { return 0; }
    @Override public int getLight() { return 0; }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Override
    public Level getWorld() {
        return this.world;
    }
    
}
