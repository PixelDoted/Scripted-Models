package me.pixeldots.scriptedmodels.platform.other;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;

public class EntityModelExtras implements IExtras {

    public LivingEntity entity;
    public PoseStack matrixStack;
    public VertexConsumer vertexConsumer;
    public Matrix3f matrixNormal;
    public Matrix4f matrixPosition;
    public int overlay;
    public int light;

    public EntityModelExtras set(LivingEntity _entity, PoseStack _matrix, VertexConsumer _vertex, Matrix3f _normal, Matrix4f _position, int _overlay, int _light) {
        this.entity = _entity; this.matrixStack = _matrix; this.vertexConsumer = _vertex; this.matrixNormal = _normal;
        this.matrixPosition = _position; this.overlay = _overlay; this.light = _light;
        return this;
    }

    @Override
    public Object getMatrix() {
        return matrixStack;
    }

    @Override
    public VertexConsumer getVertexConsumer() {
        return vertexConsumer;
    }

    @Override
    public Matrix3f getMatrixNormal() {
        return matrixNormal;
    }

    @Override
    public Matrix4f getMatrixPosition() {
        return matrixPosition;
    }

    @Override
    public int getOverlay() {
        return overlay;
    }

    @Override
    public int getLight() {
        return light;
    }

    @Override 
    public LivingEntity getEntity() { 
        return this.entity;
    }
    
    @Override public ClientLevel getWorld() { return null; }
    
}
