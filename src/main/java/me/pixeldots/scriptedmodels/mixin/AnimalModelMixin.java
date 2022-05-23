package me.pixeldots.scriptedmodels.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.script.line.Line;
import me.pixeldots.scriptedmodels.script.line.LineMode;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Mixin(AnimalModel.class)
public class AnimalModelMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
        LivingEntity entity = ScriptedModels.Rendering_Entity;
        UUID uuid = entity.getUuid();

        if (!ScriptedModels.EntityScript.containsKey(uuid)) return;

        Object[] extras = new Object[] { entity, matrices, vertices, matrices.peek().getNormalMatrix(), matrices.peek().getPositionMatrix(), overlay, light };
        for (Line line : ScriptedModels.EntityScript.get(uuid).global) {
            line.run(extras, LineMode.RENDER);
        }
    }

}
