package me.pixeldots.scriptedmodels.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.script.line.Line;
import me.pixeldots.scriptedmodels.script.line.LineMode;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        LivingEntity me = (LivingEntity)(Object)this;
        UUID uuid = me.getUuid();

        if (!ScriptedModels.EntityScript.containsKey(uuid)) return;

        Object[] extras = new Object[] { me, me.world };
        for (Line line : ScriptedModels.EntityScript.get(uuid).global) {
            line.run(extras, LineMode.TICK);
        }
    }

}