package me.pixeldots.scriptedmodels.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.RabbitEntityModel;

@Mixin(RabbitEntityModel.class)
public interface RabbitEntityModelAccessor {
    
    @Accessor("leftHindLeg") ModelPart leftHindLeg();
    @Accessor("rightHindLeg") ModelPart rightHindLeg();
    @Accessor("leftHaunch") ModelPart leftHaunch();
    @Accessor("rightHaunch") ModelPart rightHaunch();
    @Accessor("body") ModelPart body();
    @Accessor("leftFrontLeg") ModelPart leftFrontLeg();
    @Accessor("rightFrontLeg") ModelPart rightFrontLeg();
    @Accessor("tail") ModelPart tail();

    @Accessor("head") ModelPart head();
    @Accessor("rightEar") ModelPart rightEar();
    @Accessor("leftEar") ModelPart leftEar();
    @Accessor("nose") ModelPart nose();


}
