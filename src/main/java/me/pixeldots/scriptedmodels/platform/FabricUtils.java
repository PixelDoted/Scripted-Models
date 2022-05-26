package me.pixeldots.scriptedmodels.platform;

import java.util.UUID;

import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class FabricUtils {
    
	public static LivingEntity getLivingEntity(UUID uuid) {
		PlayerEntity player = ScriptedModels.minecraft.world.getPlayerByUuid(uuid);
		if (player != null) return player;

		Iterable<Entity> entities = ScriptedModels.minecraft.world.getEntities();
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity && entity.getUuid().equals(uuid)) 
				return (LivingEntity)entity;
		}
		return null;
	}

    public static EntityModel<?> getModel(LivingEntity entity) {
		if (entity == null) return null;

		LivingEntityRenderer<?,?> renderer = (LivingEntityRenderer<?,?>)ScriptedModels.minecraft.getEntityRenderDispatcher().getRenderer(entity);
		return renderer.getModel();
	}

}
