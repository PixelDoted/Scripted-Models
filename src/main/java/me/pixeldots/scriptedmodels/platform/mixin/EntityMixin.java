package me.pixeldots.scriptedmodels.platform.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(Entity.class)
public class EntityMixin {
    
    @Inject(method = "setRemoved", at = @At("TAIL"), cancellable = true)
    private void setRemoved(RemovalReason reason, CallbackInfo info) {
        if ((Object)this instanceof LivingEntity) {
            LivingEntity me = (LivingEntity)(Object)this;
            UUID uuid = me.getUuid();
            if (ScriptedModels.EntityScript.containsKey(uuid) && !(me instanceof PlayerEntity))
                ClientHelper.reset_entity(uuid);
        }
    }

}
