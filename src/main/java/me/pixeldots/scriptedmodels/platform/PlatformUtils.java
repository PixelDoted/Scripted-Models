package me.pixeldots.scriptedmodels.platform;

import java.util.UUID;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class PlatformUtils {

	/**
	 * gets a LivingEntity from a UUID
	 * @param uuid the entity's UUID
	 * @return the LivingEntity
	 */
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

	/**
	 * gets an EntityModel from a LivingEntity
	 * @param entity the LivingEntity
	 * @return the EntityModel
	 */
    public static EntityModel<?> getModel(LivingEntity entity) {
		if (entity == null) return null;

		LivingEntityRenderer<?,?> renderer = (LivingEntityRenderer<?,?>)ScriptedModels.minecraft.getEntityRenderDispatcher().getRenderer(entity);
		return renderer == null ? null : renderer.getModel();
	}

	/**
	 * gets all Head Parts from an EntityModel
	 * @param model the EntityModel
	 * @return the Head Parts
	 */
	public static Iterable<ModelPart> getHeadParts(EntityModel<?> model) {
		if (model instanceof AnimalModel)
			return ((IAnimalModelMixin)model).getHeadParts();

		return null;
	}

	/**
	 * gets all Body Parts from an EntityModel
	 * @param model the EntityModel
	 * @return the Body Parts
	 */
	public static Iterable<ModelPart> getBodyParts(EntityModel<?> model) {
		if (model instanceof AnimalModel)
			return ((IAnimalModelMixin)model).getBodyParts();

		return null;
	}

}
