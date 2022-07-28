package me.pixeldots.scriptedmodels.platform.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;

@Mixin(Entity.class)
public class EntityMixin {
    
    @Inject(method = "setRemoved", at = @At("TAIL"), cancellable = true)
    private void setRemoved(RemovalReason reason, CallbackInfo info) {
        if ((Object)this instanceof LivingEntity) {
            LivingEntity me = (LivingEntity)(Object)this;
            UUID uuid = me.getUUID();
            if (ScriptedModels.EntityScript.containsKey(uuid) && !(me instanceof Player))
                ClientHelper.reset_entity(uuid);
        }
    }

}
