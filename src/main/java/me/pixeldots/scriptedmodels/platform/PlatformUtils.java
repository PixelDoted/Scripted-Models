package me.pixeldots.scriptedmodels.platform;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.mixin.LlamaEntityModelAccessor;
import me.pixeldots.scriptedmodels.platform.mixin.RabbitEntityModelAccessor;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import net.minecraft.client.render.entity.model.RabbitEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
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
		if (model instanceof AnimalModel) // get Head Parts if the model is an Animal Model
			return ((IAnimalModelMixin)model).getHeadParts();
		else if (model instanceof LlamaEntityModel) // get Head Parts if the model is a Llama Model
			return ImmutableList.of(((LlamaEntityModelAccessor)model).head());
		else if (model instanceof RabbitEntityModel) { // get Head Parts if the model is a Rabbit Model
			RabbitEntityModelAccessor m = (RabbitEntityModelAccessor)model;
			return ImmutableList.of(m.head(), m.leftEar(), m.rightEar(), m.nose());
		}

		return ImmutableList.of();
	}

	/**
	 * gets all Body Parts from an EntityModel
	 * @param model the EntityModel
	 * @return the Body Parts
	 */
	public static Iterable<ModelPart> getBodyParts(EntityModel<?> model) {
		if (model instanceof AnimalModel) // get Body Parts if the model is an Animal Model
			return ((IAnimalModelMixin)model).getBodyParts();
		else if (model instanceof SinglePartEntityModel) // get Part if the model is a Single Part Model
			return getModelPartChildren(((SinglePartEntityModel)model).getPart());
		else if (model instanceof CompositeEntityModel) // get Parts if the model is a Composite Model
			return ((CompositeEntityModel)model).getParts();
		else if (model instanceof LlamaEntityModel) { // get Body Parts if the model is a Llama Model
			LlamaEntityModelAccessor m = (LlamaEntityModelAccessor)model;
			return ImmutableList.of(m.body(), m.rightHindLeg(), m.leftHindLeg(), m.rightFrontLeg(), m.leftFrontLeg(), m.rightChest(), m.leftChest());
		} else if (model instanceof RabbitEntityModel) { // get Body Parts if the model is a Rabbit Model
			RabbitEntityModelAccessor m = (RabbitEntityModelAccessor)model;
			return ImmutableList.of(m.body(), m.rightHindLeg(), m.leftHindLeg(), m.rightFrontLeg(), m.leftFrontLeg(), m.rightHaunch(), m.leftHaunch(), m.tail());
		}

		return ImmutableList.of();
	}

	private static Iterable<ModelPart> getModelPartChildren(ModelPart part) {
		List<ModelPart> parts = part.traverse().toList();
		if (parts.size() == 0) return ImmutableList.of(part);

		return (Iterable<ModelPart>)parts;
	}

}
