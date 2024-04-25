package me.pixeldots.scriptedmodels.platform.other;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class LivingEntityExtras implements IExtras {

    public LivingEntity entity;
    public World world;

    public LivingEntityExtras set(LivingEntity _entity, World _world) {
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
    public World getWorld() {
        return this.world;
    }
    
}
