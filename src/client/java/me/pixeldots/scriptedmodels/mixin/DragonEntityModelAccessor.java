package me.pixeldots.scriptedmodels.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer.DragonEntityModel;

@Mixin(DragonEntityModel.class)
public interface DragonEntityModelAccessor {
    
    @Accessor("head") ModelPart head();
    @Accessor("neck") ModelPart neck();
    @Accessor("jaw") ModelPart jaw();
    
    @Accessor("body") ModelPart body();
    @Accessor("leftWing") ModelPart leftWing();
    @Accessor("leftWingTip") ModelPart leftWingTip();
    @Accessor("leftFrontLeg") ModelPart leftFrontLeg();
    @Accessor("leftFrontLegTip") ModelPart leftFrontLegTip();
    @Accessor("leftFrontFoot") ModelPart leftFrontFoot();
    @Accessor("leftHindLeg") ModelPart leftHindLeg();
    @Accessor("leftHindLegTip") ModelPart leftHindLegTip();
    @Accessor("leftHindFoot") ModelPart leftHindFoot();
    @Accessor("rightWing") ModelPart rightWing();
    @Accessor("rightWingTip") ModelPart rightWingTip();
    @Accessor("rightFrontLeg") ModelPart rightFrontLeg();
    @Accessor("rightFrontLegTip") ModelPart rightFrontLegTip();
    @Accessor("rightFrontFoot") ModelPart rightFrontFoot();
    @Accessor("rightHindLeg") ModelPart rightHindLeg();
    @Accessor("rightHindLegTip") ModelPart rightHindLegTip();
    @Accessor("rightHindFoot") ModelPart rightHindFoot();
    
}
