package me.pixeldots.scriptedmodels.platform.other;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
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
