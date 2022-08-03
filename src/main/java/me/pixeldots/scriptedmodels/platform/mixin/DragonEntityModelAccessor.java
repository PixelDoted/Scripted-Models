package me.pixeldots.scriptedmodels.platform.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EnderDragonRenderer.DragonModel;

@Mixin(DragonModel.class)
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
    @Accessor("leftRearLeg") ModelPart leftRearLeg();
    @Accessor("leftRearLegTip") ModelPart leftRearLegTip();
    @Accessor("leftRearFoot") ModelPart leftRearFoot();
    @Accessor("rightWing") ModelPart rightWing();
    @Accessor("rightWingTip") ModelPart rightWingTip();
    @Accessor("rightFrontLeg") ModelPart rightFrontLeg();
    @Accessor("rightFrontLegTip") ModelPart rightFrontLegTip();
    @Accessor("rightFrontFoot") ModelPart rightFrontFoot();
    @Accessor("rightRearLeg") ModelPart rightRearLeg();
    @Accessor("rightRearLegTip") ModelPart rightRearLegTip();
    @Accessor("rightRearFoot") ModelPart rightRearFoot();
    
}
