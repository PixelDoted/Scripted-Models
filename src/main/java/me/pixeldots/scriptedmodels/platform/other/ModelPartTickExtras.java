package me.pixeldots.scriptedmodels.platform.other;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;

public class ModelPartTickExtras extends ModelPartExtras implements IExtras {
    
    public ClientLevel world;

    public ModelPartTickExtras set(LivingEntity _entity, ClientLevel _world, ModelPart _part, CallbackInfo _info) {
        this.world = _world;
        super.set(_entity, _part, _info);
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
    public ClientLevel getWorld() {
        return this.world;
    }
}
