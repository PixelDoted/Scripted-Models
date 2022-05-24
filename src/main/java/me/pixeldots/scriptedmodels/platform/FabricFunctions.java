package me.pixeldots.scriptedmodels.platform;

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

public class FabricFunctions {
    
    // tick //
    public static void particle(Object[] extra, String type, float x, float y, float z, float vx, float vy, float vz) {
        LivingEntity entity = (LivingEntity)extra[0];
        World world = (World)extra[1];
        Identifier id = new Identifier(type.toLowerCase());

        if (!Registry.PARTICLE_TYPE.containsId(id)) { return; }
        world.addParticle((ParticleEffect)Registry.PARTICLE_TYPE.get(id), x+entity.getX(), y+entity.getY(), z+entity.getZ(), vx, vy, vz);
    }

    /// render //
    public static void vertex(Object[] extra, float x, float y, float z, float nx, float ny, float nz, float u, float v, float r, float g, float b, float a) {
        VertexConsumer vc = (VertexConsumer)extra[2];
        Matrix3f normal = (Matrix3f)extra[3];
        Matrix4f model = (Matrix4f)extra[4];
        int overlay = (int)extra[5];
        int light = (int)extra[6];
        
        vc.vertex(model, x/16, y/16, z/16).color(r/255F, g/255F, b/255F, a/255F).texture(u, v).overlay(overlay).light(light).normal(normal, nx, ny, nz).next();
    }

    // transform
    public static void translate(Object[] extra, float x, float y, float z) {
        if (extra[1] instanceof MatrixStack) {
            MatrixStack matrices = (MatrixStack)extra[1];
            matrices.translate(x, y, z);
        } else {
            MatrixStack.Entry entry = (MatrixStack.Entry)extra[1];
            entry.getPositionMatrix().multiplyByTranslation(x, y, z);
        }
    }

    public static void rotate(Object[] extra, float x, float y, float z) {
        if (extra[1] instanceof MatrixStack) {
            MatrixStack matrices = (MatrixStack)extra[1];
            matrices.multiply(Quaternion.fromEulerXyz(x, y, z));
        } else {
            MatrixStack.Entry entry = (MatrixStack.Entry)extra[1];
            Quaternion quaternion = Quaternion.fromEulerXyz(x, y, z);
            entry.getPositionMatrix().multiply(quaternion);
            entry.getNormalMatrix().multiply(quaternion);
        }
    }

    public static void scale(Object[] extra, float x, float y, float z) {
        if (extra[1] instanceof MatrixStack) {
            MatrixStack matrices = (MatrixStack)extra[1];
            matrices.scale(x, y, z);
        } else {
            MatrixStack.Entry entry = (MatrixStack.Entry)extra[1];
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

}
