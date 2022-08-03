package me.pixeldots.scriptedmodels.platform;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.mixin.LlamaEntityModelAccessor;
import me.pixeldots.scriptedmodels.platform.mixin.RabbitEntityModelAccessor;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.RabbitModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;

public class PlatformUtils {

	/**
	 * gets a LivingEntity from a UUID
	 * @param uuid the entity's UUID
	 * @return the LivingEntity
	 */
	public static LivingEntity getLivingEntity(UUID uuid) {
		Player player = ScriptedModels.minecraft.level.getPlayerByUUID(uuid);
		if (player != null) return player;

		Iterable<Entity> entities = ScriptedModels.minecraft.level.entitiesForRendering();
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity && entity.getUUID().equals(uuid)) 
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

		LivingEntityRenderer<?,?> living_renderer = (LivingEntityRenderer<?,?>)ScriptedModels.minecraft.getEntityRenderDispatcher().getRenderer(entity);
		return living_renderer == null ? null : living_renderer.getModel();
	}

	/**
	 * gets all Head Parts from an EntityModel
	 * @param model the EntityModel
	 * @return the Head Parts
	 */
	public static Iterable<ModelPart> getHeadParts(EntityModel<?> model) {
		if (model instanceof AgeableListModel) // get Head Parts if the model is an Animal Model
			return ((IAnimalModelMixin)model).getHeadParts();
		else if (model instanceof LlamaModel) // get Head Parts if the model is a Llama Model
			return ImmutableList.of(((LlamaEntityModelAccessor)model).head());
		else if (model instanceof RabbitModel) { // get Head Parts if the model is a Rabbit Model
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
		if (model instanceof AgeableListModel) // get Body Parts if the model is an Animal Model
			return ((IAnimalModelMixin)model).getBodyParts();
		else if (model instanceof HierarchicalModel) // get Part if the model is a Single Part Model
			return getModelPartChildren(((HierarchicalModel<?>)model).root());
		else if (model instanceof ListModel) // get Parts if the model is a List Model (Forge)
			return ((ListModel<?>)model).parts();
		else if (model instanceof LlamaModel) { // get Body Parts if the model is a Llama Model
			LlamaEntityModelAccessor m = (LlamaEntityModelAccessor)model;
			return ImmutableList.of(m.body(), m.rightHindLeg(), m.leftHindLeg(), m.rightFrontLeg(), m.leftFrontLeg(), m.rightChest(), m.leftChest());
		} else if (model instanceof RabbitModel) { // get Body Parts if the model is a Rabbit Model
			RabbitEntityModelAccessor m = (RabbitEntityModelAccessor)model;
			return ImmutableList.of(m.body(), m.rightRearFoot(), m.leftRearFoot(), m.rightFrontLeg(), m.leftFrontLeg(), m.rightHaunch(), m.leftHaunch(), m.tail());
		}

		return ImmutableList.of();
	}

	private static Iterable<ModelPart> getModelPartChildren(ModelPart part) {
		List<ModelPart> parts = part.getAllParts().toList();
		if (parts.size() == 0) return ImmutableList.of(part);

		return (Iterable<ModelPart>)parts;
	}

}
