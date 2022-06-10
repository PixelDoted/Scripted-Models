package me.pixeldots.scriptedmodels.platform.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.other.LivingEntityExtras;
import me.pixeldots.scriptedmodels.platform.other.ModelPartTickExtras;
import me.pixeldots.scriptedmodels.script.line.Line;
import me.pixeldots.scriptedmodels.script.line.LineMode;
import me.pixeldots.scriptedmodels.script.line.LineType;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo info) {
        LivingEntity me = (LivingEntity)(Object)this;
        UUID uuid = me.getUuid();

        if (!ScriptedModels.EntityScript.containsKey(uuid)) return;

        LivingEntityExtras extras = new LivingEntityExtras().set(me, me.world);
        for (Line line : ScriptedModels.EntityScript.get(uuid).global) {
            if (line.type == LineType.CANCEL) { return; }
            line.run(extras, LineMode.TICK);
        }

        /*EntityModel<?> entitymodel = FabricUtils.getModel(me);
        if (entitymodel == null) return;

        IAnimalModelMixin model = (IAnimalModelMixin)entitymodel;
        for (ModelPart part : model.getHeadParts()) {
            run_ticked_modelpart(me, uuid, part, info);
        }
        for (ModelPart part : model.getBodyParts()) {
            run_ticked_modelpart(me, uuid, part, info);
        }*/
    }

    /*private void run_ticked_modelpart(LivingEntity entity, UUID uuid, ModelPart me, CallbackInfo info) {
        if (!ScriptedModels.EntityScript.get(uuid).parts.containsKey(me)) return;
        
        ModelPartTickExtras extras = new ModelPartTickExtras().set(entity, entity.world, me, info);
        for (Line line : ScriptedModels.EntityScript.get(uuid).parts.get(me)) {
            if (line.type == LineType.CANCEL) { return; }
            line.run(extras, LineMode.TICK);
        }
    }*/

}
