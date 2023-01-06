package me.pixeldots.scriptedmodels.platform;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import me.pixeldots.scriptedmodels.platform.other.IExtras;
import me.pixeldots.scriptedmodels.platform.other.ModelPartExtras;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PlatformFunctions {
    
    // global //

    // tick //
    public static void particle(Object extras, String type, float x, float y, float z, float vx, float vy, float vz) {
        IExtras iExtras = (IExtras)extras;
        LivingEntity entity = iExtras.getEntity();
        World world = iExtras.getWorld();
        Identifier id = new Identifier(type.toLowerCase());

        if (!Registries.PARTICLE_TYPE.containsId(id)) { return; }
        
        float yaw = (float)Math.toRadians(entity.getYaw());
        float xOff = (x*(float)Math.cos(yaw))-(z*(float)Math.sin(yaw)), zOff = (z*(float)Math.cos(yaw))+(x*(float)Math.sin(yaw));
        float xVOff = (vx*(float)Math.cos(yaw))-(vz*(float)Math.sin(yaw)), zVOff = (vz*(float)Math.cos(yaw))+(vx*(float)Math.sin(yaw));
        world.addParticle((ParticleEffect)Registries.PARTICLE_TYPE.get(id), xOff+entity.getX(), y+entity.getY(), zOff+entity.getZ(), xVOff, vy, zVOff);
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
            entry.getPositionMatrix().mul(new Matrix4f().translate(x, y, z));
        }
    }

    public static void rotate(Object extras, float x, float y, float z) {
        Object matrix = ((IExtras)extras).getMatrix();
        if (matrix instanceof MatrixStack) {
            MatrixStack matrices = (MatrixStack)matrix;
            matrices.multiply(new Quaternionf().rotateXYZ(x, y, z));
        } else {
            MatrixStack.Entry entry = (MatrixStack.Entry)matrix;
            Quaternionf quaternion = new Quaternionf().rotateXYZ(x, y, z);
            entry.getPositionMatrix().rotate(quaternion);
            entry.getNormalMatrix().rotate(quaternion);
        }
    }

    public static void scale(Object extras, float x, float y, float z) {
        Object matrix = ((IExtras)extras).getMatrix();
        if (matrix instanceof MatrixStack) {
            MatrixStack matrices = (MatrixStack)matrix;
            matrices.scale(x, y, z);
        } else {
            MatrixStack.Entry entry = (MatrixStack.Entry)matrix;
            entry.getPositionMatrix().mul(new Matrix4f().scale(x, y, z));
            if (x == y && y == z) {
                if (x > 0.0f) return;
                entry.getNormalMatrix().scale(-1.0f);
            }
            float f = 1.0f / x, g = 1.0f / y, h = 1.0f / z;
            float i = MathHelper.fastInverseCbrt(f * g * h);
            entry.getNormalMatrix().mul(new Matrix3f().scale(i * f, i * g, i * h));
        }
    }

    public static void angle(Object extras, float pitch, float yaw, float roll) {
        if (extras instanceof ModelPartExtras)
            ((ModelPartExtras)extras).modelPart.setAngles(pitch, yaw, roll);
    }

}
