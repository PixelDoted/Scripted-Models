package me.pixeldots.scriptedmodels.platform.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.other.EntityModelExtras;
import me.pixeldots.scriptedmodels.script.line.Line;
import me.pixeldots.scriptedmodels.script.line.LineMode;
import me.pixeldots.scriptedmodels.script.line.LineType;
import net.minecraft.world.entity.LivingEntity;

@Mixin(AnimalModel.class)
public class AnimalModelMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
        LivingEntity entity = ScriptedModels.Rendering_Entity;
        UUID uuid = entity.getUUID();

        if (!ScriptedModels.EntityScript.containsKey(uuid)) return;

        EntityModelExtras extras = new EntityModelExtras().set(entity, matrices, vertices, matrices.last().normal(), matrices.last().pose(), overlay, light);
        for (Line line : ScriptedModels.EntityScript.get(uuid).global) {
            if (line.type == LineType.CANCEL) { info.cancel(); return; }
            line.run(extras, LineMode.RENDER);
        }
    }

}
