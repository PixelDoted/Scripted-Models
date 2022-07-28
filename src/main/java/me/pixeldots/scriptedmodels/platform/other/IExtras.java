package me.pixeldots.scriptedmodels.platform.other;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface IExtras {
    Object getMatrix();
    VertexConsumer getVertexConsumer();
    Matrix3f getMatrixNormal();
    Matrix4f getMatrixPosition();
    int getOverlay();
    int getLight();

    LivingEntity getEntity();
    Level getWorld();
}
