package me.pixeldots.scriptedmodels.platform.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.model.geom.ModelPart;

@Mixin(AnimalModel.class)
public interface IAnimalModelMixin {
    @Invoker("getHeadParts")
    public Iterable<ModelPart> getHeadParts();
    @Invoker("getBodyParts")
    public Iterable<ModelPart> getBodyParts();
}
