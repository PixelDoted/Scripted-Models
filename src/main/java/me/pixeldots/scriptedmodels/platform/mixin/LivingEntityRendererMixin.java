package me.pixeldots.scriptedmodels.platform.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    
    @Inject(method = "render", at = @At("HEAD"))
    private void render(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        ScriptedModels.Rendering_Entity = livingEntity;
    }

}
