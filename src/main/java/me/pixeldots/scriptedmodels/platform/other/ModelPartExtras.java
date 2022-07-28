package me.pixeldots.scriptedmodels.platform.other;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class ModelPartExtras {
    
    public LivingEntity entity;
    public ModelPart modelPart;
    public CallbackInfo info;

    public ModelPartExtras set(LivingEntity _entity, ModelPart _part, CallbackInfo _info) {
        this.entity = _entity;
        this.modelPart = _part;
        this.info = _info;
        return this;
    }

}
