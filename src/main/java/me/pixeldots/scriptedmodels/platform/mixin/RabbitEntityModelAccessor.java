package me.pixeldots.scriptedmodels.platform.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.RabbitModel;
import net.minecraft.client.model.geom.ModelPart;

@Mixin(RabbitModel.class)
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
