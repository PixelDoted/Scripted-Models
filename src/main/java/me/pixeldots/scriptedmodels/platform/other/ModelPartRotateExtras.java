package me.pixeldots.scriptedmodels.platform.other;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

public class ModelPartRotateExtras extends ModelPartExtras implements IExtras {

    public MatrixStack matrix;
    public Matrix3f matrixNormal;
    public Matrix4f matrixPosition;

    public ModelPartRotateExtras set(LivingEntity _entity, MatrixStack _matrix, Matrix3f _matrixnormal, Matrix4f _matrixposition, CallbackInfo _info, ModelPart _modelpart) {
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

    @Override public World getWorld() { return null; }
    
}
