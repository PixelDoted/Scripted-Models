package me.pixeldots.scriptedmodels.platform;

import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

public class FabricUtils {
    
    public static EntityModel<?> getModel(LivingEntity entity) {
		LivingEntityRenderer<?,?> renderer = (LivingEntityRenderer<?,?>)ScriptedModels.minecraft.getEntityRenderDispatcher().getRenderer(entity);
		return renderer.getModel();
	}

}
