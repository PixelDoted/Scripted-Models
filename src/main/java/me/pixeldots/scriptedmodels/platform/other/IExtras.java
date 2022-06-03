package me.pixeldots.scriptedmodels.platform.other;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public interface IExtras {
    Object getMatrix();
    VertexConsumer getVertexConsumer();
    Matrix3f getMatrixNormal();
    Matrix4f getMatrixPosition();
    int getOverlay();
    int getLight();
}
