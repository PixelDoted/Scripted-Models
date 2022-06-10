package me.pixeldots.scriptedmodels.platform;

import me.pixeldots.scriptedmodels.platform.other.IExtras;
import me.pixeldots.scriptedmodels.platform.other.ModelPartRenderExtras;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class PlatformFunctions {
    
    // global //

    // tick //
    public static void particle(Object extras, String type, float x, float y, float z, float vx, float vy, float vz) {
        IExtras iExtras = (IExtras)extras;
        LivingEntity entity = iExtras.getEntity();
        World world = iExtras.getWorld();
        Identifier id = new Identifier(type.toLowerCase());

        if (!Registry.PARTICLE_TYPE.containsId(id)) { return; }
        
        float yaw = (float)Math.toRadians(entity.getYaw());
        float xOff = (x*(float)Math.cos(yaw))-(z*(float)Math.sin(yaw)), zOff = (z*(float)Math.cos(yaw))+(x*(float)Math.sin(yaw));
        float xVOff = (vx*(float)Math.cos(yaw))-(vz*(float)Math.sin(yaw)), zVOff = (vz*(float)Math.cos(yaw))+(vx*(float)Math.sin(yaw));
        world.addParticle((ParticleEffect)Registry.PARTICLE_TYPE.get(id), xOff+entity.getX(), y+entity.getY(), zOff+entity.getZ(), xVOff, vy, zVOff);
    }

    /// render //
    public static void vertex(Object extras, float x, float y, float z, float nx, float ny, float nz, float u, float v, float r, float g, float b, float a) {
        IExtras iExtras = (IExtras)extras;
        VertexConsumer vc = iExtras.getVertexConsumer();
        Matrix3f normal = iExtras.getMatrixNormal();
        Matrix4f position = iExtras.getMatrixPosition();
        int overlay = iExtras.getOverlay();
        int light = iExtras.getLight();
        
        vc.vertex(position, x/16, y/16, z/16).color(r/255F, g/255F, b/255F, a/255F).texture(u, v).overlay(overlay).light(light).normal(normal, nx, ny, nz).next();
    }

    // transform
    public static void translate(Object extras, float x, float y, float z) {
        Object matrix = ((IExtras)extras).getMatrix();
        if (matrix instanceof MatrixStack) {
            MatrixStack matrices = (MatrixStack)matrix;
            matrices.translate(x, y, z);
        } else {
            MatrixStack.Entry entry = (MatrixStack.Entry)matrix;
            entry.getPositionMatrix().multiplyByTranslation(x, y, z);
        }
    }

    public static void rotate(Object extras, float x, float y, float z) {
        Object matrix = ((IExtras)extras).getMatrix();
        if (matrix instanceof MatrixStack) {
            MatrixStack matrices = (MatrixStack)matrix;
            matrices.multiply(Quaternion.fromEulerXyz(x, y, z));
        } else {
            MatrixStack.Entry entry = (MatrixStack.Entry)matrix;
            Quaternion quaternion = Quaternion.fromEulerXyz(x, y, z);
            entry.getPositionMatrix().multiply(quaternion);
            entry.getNormalMatrix().multiply(quaternion);
        }
    }

    public static void scale(Object extras, float x, float y, float z) {
        Object matrix = ((IExtras)extras).getMatrix();
        if (matrix instanceof MatrixStack) {
            MatrixStack matrices = (MatrixStack)matrix;
            matrices.scale(x, y, z);
        } else {
            MatrixStack.Entry entry = (MatrixStack.Entry)matrix;
            entry.getPositionMatrix().multiply(Matrix4f.scale(x, y, z));
            if (x == y && y == z) {
                if (x > 0.0f) return;
                entry.getNormalMatrix().multiply(-1.0f);
            }
            float f = 1.0f / x, g = 1.0f / y, h = 1.0f / z;
            float i = MathHelper.fastInverseCbrt(f * g * h);
            entry.getNormalMatrix().multiply(Matrix3f.scale(i * f, i * g, i * h));
        }
    }

    public static void angle(Object extras, float pitch, float yaw, float roll) {
        if (extras instanceof ModelPartRenderExtras)
            ((ModelPartRenderExtras)extras).modelPart.setAngles((float)Math.toRadians(pitch), (float)Math.toRadians(yaw), (float)Math.toRadians(roll));
    }

}
