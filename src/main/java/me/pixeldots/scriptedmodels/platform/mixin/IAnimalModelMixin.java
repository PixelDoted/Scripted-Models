package me.pixeldots.scriptedmodels.platform.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;

@Mixin(AgeableListModel.class)
public interface IAnimalModelMixin {
    @Invoker("headParts")
    public Iterable<ModelPart> getHeadParts();
    @Invoker("bodyParts")
    public Iterable<ModelPart> getBodyParts();
}
