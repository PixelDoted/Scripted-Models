package me.pixeldots.scriptedmodels.mixin;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.scriptedmodels.ScriptedModelsClient;
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
public abstract class ModelPartMixin {
    @Accessor("children")
    abstract Map<String, ModelPart> getChildren();

    @Invoker("renderCuboids")
    abstract void invokeRenderCuboids(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha);

    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
        // HACK: This replaces the original render function
        
        ModelPart self = (ModelPart)(Object)this;
        if (!self.visible) {
            info.cancel();
            return;
        }

        matrices.push();
        self.rotate(matrices);
        if (!this.renderAdditions(matrices.peek(), vertices, light, overlay, info)) {
            return;
        }

        if (!self.hidden) {
            this.invokeRenderCuboids(matrices.peek(), vertices, light, overlay, red, green, blue, alpha);
        }
        for (ModelPart modelPart : this.getChildren().values()) {
            modelPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }

        matrices.pop();
        info.cancel();
    }

    private boolean renderAdditions(MatrixStack.Entry entry, VertexConsumer vertices, int light, int overlay, CallbackInfo info) {
        if (ScriptedModelsClient.Rendering_Entity == null) return true;
        LivingEntity entity = ScriptedModelsClient.Rendering_Entity;
        UUID uuid = entity.getUuid();

        if (!ScriptedModelsClient.EntityScript.containsKey(uuid)) return true;

        ModelPart me = (ModelPart)(Object)this;
        if (!ScriptedModelsClient.EntityScript.get(uuid).parts.containsKey(me)) return true;
        
        ModelPartRenderExtras extras = new ModelPartRenderExtras().set(entity, entry, vertices, entry.getNormalMatrix(), entry.getPositionMatrix(), overlay, light, info, me);
        for (Line line : ScriptedModelsClient.EntityScript.get(uuid).parts.get(me)) {
            if (line.type == LineType.CANCEL) { info.cancel(); return false; }
            line.run(extras, LineMode.RENDER);
        }

        return true;
    }

    @Inject(method = "rotate", at = @At("HEAD"), cancellable = true)
    private void rotate(MatrixStack matrices, CallbackInfo info) {
        if (ScriptedModelsClient.Rendering_Entity == null) return;
        LivingEntity entity = ScriptedModelsClient.Rendering_Entity;
        UUID uuid = entity.getUuid();
        if (!ScriptedModelsClient.EntityScript.containsKey(uuid)) return;

        ModelPart me = (ModelPart)(Object)this;
        if (!ScriptedModelsClient.EntityScript.get(uuid).parts.containsKey(me)) return;
        
        ModelPartRotateExtras extras = new ModelPartRotateExtras().set(entity, matrices, matrices.peek().getNormalMatrix(), matrices.peek().getPositionMatrix(), info, me);
        for (Line line : ScriptedModelsClient.EntityScript.get(uuid).parts.get(me)) {
            line.run(extras, LineMode.ROTATE);
        }
    }
    
}
