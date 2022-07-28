package me.pixeldots.scriptedmodels.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;

import me.pixeldots.scriptedmodels.platform.other.IExtras;
import me.pixeldots.scriptedmodels.platform.other.ModelPartExtras;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PlatformFunctions {
    
    // global //

    // tick //
    public static void particle(Object extras, String type, float x, float y, float z, float vx, float vy, float vz) {
        IExtras iExtras = (IExtras)extras;
        LivingEntity entity = iExtras.getEntity();
        Level world = iExtras.getWorld();
        ResourceLocation id = new ResourceLocation(type.toLowerCase());

        if (!Registry.PARTICLE_TYPE.containsKey(id)) { return; }
        
        float yaw = (float)Math.toRadians(entity.getYHeadRot());
        float xOff = (x*(float)Math.cos(yaw))-(z*(float)Math.sin(yaw)), zOff = (z*(float)Math.cos(yaw))+(x*(float)Math.sin(yaw));
        float xVOff = (vx*(float)Math.cos(yaw))-(vz*(float)Math.sin(yaw)), zVOff = (vz*(float)Math.cos(yaw))+(vx*(float)Math.sin(yaw));
        world.addParticle((ParticleOptions)Registry.PARTICLE_TYPE.get(id), xOff+entity.getX(), y+entity.getY(), zOff+entity.getZ(), xVOff, vy, zVOff);
    }

    /// render //
    public static void vertex(Object extras, float x, float y, float z, float nx, float ny, float nz, float u, float v, float r, float g, float b, float a) {
        IExtras iExtras = (IExtras)extras;
        VertexConsumer vc = iExtras.getVertexConsumer();
        Matrix3f normal = iExtras.getMatrixNormal();
        Matrix4f position = iExtras.getMatrixPosition();
        int overlay = iExtras.getOverlay();
        int light = iExtras.getLight();
        
        vc.vertex(position, x/16, y/16, z/16).color(r/255F, g/255F, b/255F, a/255F).uv(u, v).overlayCoords(overlay).uv2(light).normal(normal, nx, ny, nz).endVertex();
    }

    // transform
    public static void translate(Object extras, float x, float y, float z) {
        Object matrix = ((IExtras)extras).getMatrix();
        if (matrix instanceof PoseStack) {
            PoseStack matrices = (PoseStack)matrix;
            matrices.translate(x, y, z);
        } else {
            PoseStack.Pose entry = (PoseStack.Pose)matrix;
            entry.pose().multiplyWithTranslation(x, y, z);
        }
    }

    public static void rotate(Object extras, float x, float y, float z) {
        Object matrix = ((IExtras)extras).getMatrix();
        if (matrix instanceof PoseStack) {
            PoseStack matrices = (PoseStack)matrix;
            matrices.mulPose(Quaternion.fromYXZ(y, x, z));
        } else {
            PoseStack.Pose entry = (PoseStack.Pose)matrix;
            Quaternion quaternion = Quaternion.fromYXZ(y, x, z);
            entry.pose().multiply(quaternion);
            entry.normal().mul(quaternion);
        }
    }

    public static void scale(Object extras, float x, float y, float z) {
        Object matrix = ((IExtras)extras).getMatrix();
        if (matrix instanceof PoseStack) {
            PoseStack matrices = (PoseStack)matrix;
            matrices.scale(x, y, z);
        } else {
            PoseStack.Pose posestack$pose = (PoseStack.Pose)matrix;
            posestack$pose.pose().multiply(Matrix4f.createScaleMatrix(x, y, z));
            if (x == y && y == z) {
                if (x > 0.0F) {
                    return;
                }

                posestack$pose.normal().mul(-1.0F);
            }

            float f = 1.0F / x;
            float f1 = 1.0F / y;
            float f2 = 1.0F / z;
            float f3 = Mth.fastInvCubeRoot(f * f1 * f2);
            posestack$pose.normal().mul(Matrix3f.createScaleMatrix(f3 * f, f3 * f1, f3 * f2));
        }
    }

    public static void angle(Object extras, float pitch, float yaw, float roll) {
        if (extras instanceof ModelPartExtras)
            ((ModelPartExtras)extras).modelPart.setRotation(pitch, yaw, roll);
    }

}
