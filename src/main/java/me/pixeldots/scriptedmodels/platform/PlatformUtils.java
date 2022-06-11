package me.pixeldots.scriptedmodels.platform;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class PlatformUtils {

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
		return renderer == null ? null : renderer.getModel();
	}

	/*
	 * This can cause Lag
	 * only use this when needed
	 */
	public static List<ModelPart> getModelParts(EntityModel<LivingEntity> model) {
		List<ModelPart> parts = new ArrayList<>();

        Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isInstance(ModelPart.class)) {
				try {
					parts.add((ModelPart)field.get(model));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return parts;
    }

}
