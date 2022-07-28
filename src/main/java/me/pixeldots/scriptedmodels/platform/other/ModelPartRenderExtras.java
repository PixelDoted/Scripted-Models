package me.pixeldots.scriptedmodels.platform.other;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;

public class ModelPartRenderExtras extends ModelPartExtras implements IExtras {
    
    public PoseStack.Pose matrixEntry;
    public VertexConsumer vertexConsumer;
    public Matrix3f matrixNormal;
    public Matrix4f matrixPosition;
    public int overlay;
    public int light;

    public ModelPartRenderExtras set(LivingEntity _entity, PoseStack.Pose _entry, VertexConsumer _vertex, Matrix3f _normal, Matrix4f _position, int _overlay, int _light, CallbackInfo _info, ModelPart _part) {
        this.matrixEntry = _entry; this.vertexConsumer = _vertex; this.matrixNormal = _normal;
        this.matrixPosition = _position; this.overlay = _overlay; this.light = _light;
        super.set(_entity, _part, _info);
        return this;
    }

    @Override
    public Object getMatrix() {
        return this.matrixEntry;
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
