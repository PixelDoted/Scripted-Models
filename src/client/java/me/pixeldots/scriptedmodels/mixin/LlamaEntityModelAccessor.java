package me.pixeldots.scriptedmodels.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.LlamaEntityModel;

@Mixin(LlamaEntityModel.class)
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
