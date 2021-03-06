package me.pixeldots.scriptedmodels.platform.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.other.ModelPartRenderExtras;
import me.pixeldots.scriptedmodels.platform.other.ModelPartRotateExtras;
import me.pixeldots.scriptedmodels.script.line.Line;
import me.pixeldots.scriptedmodels.script.line.LineMode;
import me.pixeldots.scriptedmodels.script.line.LineType;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Mixin(ModelPart.class)
public class ModelPartMixin {

    @Inject(method = "renderCuboids", at = @At("HEAD"), cancellable = true)
    private void renderCuboids(MatrixStack.Entry entry, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
        if (ScriptedModels.Rendering_Entity == null) return;
        LivingEntity entity = ScriptedModels.Rendering_Entity;
        UUID uuid = entity.getUuid();
        if (!ScriptedModels.EntityScript.containsKey(uuid)) return;

        ModelPart me = (ModelPart)(Object)this;
        if (!ScriptedModels.EntityScript.get(uuid).parts.containsKey(me)) return;
        
        ModelPartRenderExtras extras = new ModelPartRenderExtras().set(entity, entry, vertices, entry.getNormalMatrix(), entry.getPositionMatrix(), overlay, light, info, me);
        for (Line line : ScriptedModels.EntityScript.get(uuid).parts.get(me)) {
            if (line.type == LineType.CANCEL) { info.cancel(); return; }
            line.run(extras, LineMode.RENDER);
        }
    }

    @Inject(method = "rotate", at = @At("HEAD"), cancellable = true)
    private void rotate(MatrixStack matrices, CallbackInfo info) {
        if (ScriptedModels.Rendering_Entity == null) return;
        LivingEntity entity = ScriptedModels.Rendering_Entity;
        UUID uuid = entity.getUuid();
        if (!ScriptedModels.EntityScript.containsKey(uuid)) return;

        ModelPart me = (ModelPart)(Object)this;
        if (!ScriptedModels.EntityScript.get(uuid).parts.containsKey(me)) return;
        
        ModelPartRotateExtras extras = new ModelPartRotateExtras().set(entity, matrices, matrices.peek().getNormalMatrix(), matrices.peek().getPositionMatrix(), info, me);
        for (Line line : ScriptedModels.EntityScript.get(uuid).parts.get(me)) {
            line.run(extras, LineMode.ROTATE);
        }
    }
    
}
