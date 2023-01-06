package me.pixeldots.scriptedmodels.platform.other;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public interface IExtras {
    Object getMatrix();
    VertexConsumer getVertexConsumer();
    Matrix3f getMatrixNormal();
    Matrix4f getMatrixPosition();
    int getOverlay();
    int getLight();

    LivingEntity getEntity();
    World getWorld();
}
