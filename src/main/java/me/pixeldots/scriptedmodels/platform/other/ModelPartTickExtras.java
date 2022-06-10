package me.pixeldots.scriptedmodels.platform.other;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

public class ModelPartTickExtras implements IExtras {
    public LivingEntity entity;
    public World world;
    public ModelPart modelPart;
    public CallbackInfo info;

    public ModelPartTickExtras set(LivingEntity _entity, World _world, ModelPart _part, CallbackInfo _info) {
        this.entity = _entity; this.world = _world;
        this.modelPart = _part; this.info = _info;
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
