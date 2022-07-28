package me.pixeldots.scriptedmodels.platform.other;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;

public class ModelPartRotateExtras extends ModelPartExtras implements IExtras {

    public PoseStack matrix;
    public Matrix3f matrixNormal;
    public Matrix4f matrixPosition;

    public ModelPartRotateExtras set(LivingEntity _entity, PoseStack _matrix, Matrix3f _matrixnormal, Matrix4f _matrixposition, CallbackInfo _info, ModelPart _modelpart) {
        this.matrix = _matrix; this.matrixNormal = _matrixnormal; this.matrixPosition = _matrixposition;
        super.set(_entity, _modelpart, _info);
        return this;
    }

    @Override
    public Object getMatrix() {
        return this.matrix;
    }

    @Override public VertexConsumer getVertexConsumer() { return null; }

    @Override
    public Matrix3f getMatrixNormal() {
        return this.matrixNormal;
    }

    @Override
    public Matrix4f getMatrixPosition() {
        return this.matrixPosition;
    }

    @Override public int getOverlay() { return 0;  }
    @Override public int getLight() { return 0; }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Override public ClientLevel getWorld() { return null; }
    
}
