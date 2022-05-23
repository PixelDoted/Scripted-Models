package me.pixeldots.scriptedmodels.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    
    @Inject(method = "render", at = @At("HEAD"))
    public void render(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        ScriptedModels.Rendering_Entity = livingEntity;
        if (!ScriptedModels.EntityModels.containsKey(livingEntity))
            ScriptedModels.EntityModels.put(livingEntity, ((LivingEntityRenderer<?,?>)(Object)this).getModel());
    }

    @Inject(method = "hasLabel", at = @At("HEAD"), cancellable = true)
    public void hasLabel(LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
        UUID uuid = entity.getUuid();

        if (!ScriptedModels.EntityScript.containsKey(uuid)) return;

        if (ScriptedModels.EntityScript.get(uuid).show_NameTag)
            info.setReturnValue(false);
    }

}
