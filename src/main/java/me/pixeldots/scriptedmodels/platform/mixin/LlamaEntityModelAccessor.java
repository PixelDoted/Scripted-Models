package me.pixeldots.scriptedmodels.platform.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelPart;

@Mixin(LlamaModel.class)
public interface LlamaEntityModelAccessor {
    
    @Accessor("head") ModelPart head();
    
    @Accessor("body") ModelPart body();
    @Accessor("rightHindLeg") ModelPart rightHindLeg();
    @Accessor("leftHindLeg") ModelPart leftHindLeg();
    @Accessor("rightFrontLeg") ModelPart rightFrontLeg();
    @Accessor("leftFrontLeg") ModelPart leftFrontLeg();
    @Accessor("rightChest") ModelPart rightChest();
    @Accessor("leftChest") ModelPart leftChest();

}
