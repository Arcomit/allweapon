package mod.arcomit.allweapon.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.arcomit.allweapon.entity.HuanYingDieEntity;
import mod.arcomit.allweapon.init.AwRenderTypes;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 幻影蝶渲染器 - 带拖尾效果的飞行武器渲染
 * @author Arcomit
 */
public class HuanYingDieRenderer<T extends HuanYingDieEntity> extends EntityRenderer<T> {

    public HuanYingDieRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(@Nonnull T pEntity, float pEntityYaw, float pPartialTick, @Nonnull PoseStack pPoseStack,
                       @Nonnull MultiBufferSource pBuffer, int pPackedLight) {
        // 渲染拖尾效果
        pPoseStack.pushPose();
        renderTrail(pEntity, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.popPose();

        // 渲染实体模型
        try (MSAutoCloser ignored = MSAutoCloser.pushMatrix(pPoseStack)) {
            adjustPositionForAttachedEntity(pEntity, pPartialTick, pPoseStack);
            applyEntityRotation(pEntity, pPartialTick, pPoseStack);
            renderBladeModel(pEntity, pPoseStack, pBuffer, pPackedLight);
        }
    }

    /**
     * 调整附着实体的位置，消除抖动
     */
    private void adjustPositionForAttachedEntity(T entity, float partialTick, PoseStack poseStack) {
        Entity hitEntity = entity.getAttachedEntity();
        if (hitEntity == null) return;

        Vec3 targetPos = new Vec3(
            Mth.lerp(partialTick, hitEntity.xOld, hitEntity.getX()),
            Mth.lerp(partialTick, hitEntity.yOld, hitEntity.getY()) + hitEntity.getEyeHeight() * 0.5,
            Mth.lerp(partialTick, hitEntity.zOld, hitEntity.getZ())
        );

        Vec3 currentPos = new Vec3(
            Mth.lerp(partialTick, entity.xOld, entity.getX()),
            Mth.lerp(partialTick, entity.yOld, entity.getY()),
            Mth.lerp(partialTick, entity.zOld, entity.getZ())
        );

        Vec3 offset = targetPos.subtract(currentPos);
        poseStack.translate(offset.x, offset.y, offset.z);

        float yawCorrection = Mth.rotLerp(partialTick, hitEntity.yRotO, hitEntity.getYRot()) - entity.getYawCorrection();
        poseStack.mulPose(Axis.YP.rotationDegrees(-yawCorrection));
    }

    /**
     * 应用实体旋转，包括翻转动画
     */
    private void applyEntityRotation(T entity, float partialTick, PoseStack poseStack) {
        float yRot = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        float xRot = Mth.rotLerp(partialTick, entity.xRotO, entity.getXRot());
        float zRot = entity.getRoll();

        // 计算翻转动画
        float flipRotation = (float) Math.sin((entity.tickCount + partialTick) * Math.PI / 20.0f) * 30.0f;

        Quaternionf rotation = new Quaternionf()
            .rotateY((float) Math.toRadians(yRot))
            .rotateX((float) Math.toRadians(-xRot))
            .rotateZ((float) Math.toRadians(zRot + flipRotation));

        poseStack.mulPose(rotation);
    }

    /**
     * 渲染刀刃模型
     */
    private void renderBladeModel(T entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float scale = 0.0075f;
        poseStack.scale(scale, scale, scale);

        WavefrontObject model = BladeModelManager.getInstance().getModel(getModelLocation(entity));
        ResourceLocation texture = getTextureLocation(entity);

        BladeRenderState.setCol(entity.getColor(), false);
        BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "HuanYingDie", texture,
                poseStack, buffer, packedLight);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull T summonedSwordEntity) {
        return summonedSwordEntity.getTexture();
    }

    public ResourceLocation getModelLocation(T summonedSwordEntity) {
        return summonedSwordEntity.getModel();
    }

    /**
     * 渲染拖尾效果 - 主入口方法
     */
    private void renderTrail(T entity, float partialTicks, PoseStack matrixStack,
                            MultiBufferSource bufferIn, int packedLightIn) {
        List<Vec3> trail = entity.getTrailPositions();
        if (trail == null || trail.size() < 2) return;

        TrailRenderContext context = createTrailRenderContext(entity, partialTicks, bufferIn);
        renderTrailWithContext(trail, matrixStack, context, packedLightIn);
    }

    /**
     * 创建拖尾渲染上下文
     */
    private TrailRenderContext createTrailRenderContext(T entity, float partialTicks, MultiBufferSource bufferIn) {
        ResourceLocation tex = getTextureLocation(entity);
        VertexConsumer builder = bufferIn.getBuffer(AwRenderTypes.getQuadsBlendLuminous(tex));
        Vec3 camPos = this.entityRenderDispatcher.camera.getPosition();
        Vec3 entityPos = getInterpolatedEntityPosition(entity, partialTicks);
        TrailColor color = new TrailColor(entity.getColor());

        return new TrailRenderContext(builder, camPos, entityPos, color);
    }

    /**
     * 使用上下文渲染拖尾
     */
    private void renderTrailWithContext(List<Vec3> trail, PoseStack matrixStack,
                                       TrailRenderContext context, int packedLight) {
        Vec3[] tangents = calculateTangents(trail);
        Vec3[] rightVectors = calculateRightVectors(trail, tangents, context);
        renderTrailSegments(trail, tangents, rightVectors, matrixStack, context, packedLight);
    }

    /**
     * 预计算所有拖尾点的右向量，确保连续性
     */
    private Vec3[] calculateRightVectors(List<Vec3> trail, Vec3[] tangents, TrailRenderContext context) {
        int count = trail.size();
        Vec3[] rightVectors = new Vec3[count];

        for (int i = 0; i < count; i++) {
            Vec3 pos = trail.get(i);
            Vec3 tangent = tangents[i];
            Vec3 r = pos.subtract(context.entityPos);
            Vec3 viewDir = context.camPos.subtract(context.entityPos).subtract(r);

            Vec3 right = viewDir.cross(tangent);

            if (right.length() <= 1e-6) {
                // 使用摄像机方向作为备选
                Quaternionf camOrient = this.entityRenderDispatcher.cameraOrientation();
                org.joml.Vector3f tmp = new org.joml.Vector3f(1f, 0f, 0f);
                camOrient.transform(tmp);
                right = new Vec3(tmp.x(), tmp.y(), tmp.z());
            }

            rightVectors[i] = right.normalize();
        }

        // 平滑右向量，减少突变
        smoothRightVectors(rightVectors);

        return rightVectors;
    }

    /**
     * 平滑右向量，避免相邻段之间的突变
     */
    private void smoothRightVectors(Vec3[] rightVectors) {
        if (rightVectors.length < 2) return;

        Vec3[] smoothed = new Vec3[rightVectors.length];
        smoothed[0] = rightVectors[0];

        for (int i = 1; i < rightVectors.length - 1; i++) {
            // 使用加权平均平滑
            Vec3 prev = rightVectors[i - 1];
            Vec3 curr = rightVectors[i];
            Vec3 next = rightVectors[i + 1];

            // 检查方向一致性，如果反向则翻转
            if (curr.dot(prev) < 0) {
                curr = curr.scale(-1);
            }
            if (next.dot(curr) < 0) {
                next = next.scale(-1);
            }

            smoothed[i] = prev.scale(0.25).add(curr.scale(0.5)).add(next.scale(0.25)).normalize();
        }

        smoothed[rightVectors.length - 1] = rightVectors[rightVectors.length - 1];

        // 复制回原数组
        System.arraycopy(smoothed, 0, rightVectors, 0, smoothed.length);
    }

    /**
     * 获取插值后的实体位置
     */
    private Vec3 getInterpolatedEntityPosition(T entity, float partialTicks) {
        return new Vec3(
            Mth.lerp(partialTicks, entity.xOld, entity.getX()),
            Mth.lerp(partialTicks, entity.yOld, entity.getY()),
            Mth.lerp(partialTicks, entity.zOld, entity.getZ())
        );
    }

    /**
     * 计算拖尾每个点的切线
     */
    private Vec3[] calculateTangents(List<Vec3> trail) {
        int count = trail.size();
        Vec3[] tangents = new Vec3[count];

        for (int i = 0; i < count; i++) {
            Vec3 prev = trail.get(Math.max(0, i - 1));
            Vec3 next = trail.get(Math.min(count - 1, i + 1));
            Vec3 t = next.subtract(prev);

            if (t.length() <= 1e-6) {
                t = (i < count - 1)
                    ? trail.get(i + 1).subtract(trail.get(i))
                    : trail.get(i).subtract(trail.get(i - 1));
            }
            tangents[i] = t.normalize();
        }
        return tangents;
    }

    /**
     * 渲染拖尾的所有分段
     */
    private void renderTrailSegments(List<Vec3> trail, Vec3[] tangents, Vec3[] rightVectors,
                                      PoseStack matrixStack, TrailRenderContext context, int packedLight) {
        int count = trail.size();
        Matrix4f mat = matrixStack.last().pose();
        Matrix3f normal = matrixStack.last().normal();

        for (int i = 0; i < count - 1; i++) {
            float t0 = (float) i / Math.max(1, count - 1);
            float t1 = (float) (i + 1) / Math.max(1, count - 1);

            renderTrailSegment(trail.get(i), trail.get(i + 1), rightVectors[i], rightVectors[i + 1],
                              t0, t1, mat, normal, context, packedLight);
        }
    }

    /**
     * 渲染单个拖尾分段
     */
    private void renderTrailSegment(Vec3 p0, Vec3 p1, Vec3 right0, Vec3 right1,
                                     float t0, float t1, Matrix4f mat, Matrix3f normal,
                                     TrailRenderContext context, int packedLight) {
        Vec3 r0 = p0.subtract(context.entityPos);
        Vec3 r1 = p1.subtract(context.entityPos);

        // 计算分段宽度（带尖锐化效果）
        float baseSize = 0.2f;
        float sharpenStart = 0.75f;
        float outerHalf0 = calculateSegmentWidth(baseSize, t0, sharpenStart);
        float outerHalf1 = calculateSegmentWidth(baseSize, t1, sharpenStart);

        // 使用预计算的右向量计算四个顶点位置，确保精确对齐
        Vec3 o0a = r0.add(right0.scale(outerHalf0));
        Vec3 o0b = r0.subtract(right0.scale(outerHalf0));
        Vec3 o1a = r1.add(right1.scale(outerHalf1));
        Vec3 o1b = r1.subtract(right1.scale(outerHalf1));

        // 计算颜色（带指数衰减）
        float[] color0 = context.color.calculateColor(t0, 0.35f, 0.85f, 1.5);
        float[] color1 = context.color.calculateColor(t1, 0.35f, 0.65f, 1.5);

        // 渲染四边形，确保顶点顺序一致
        addVertex(context.builder, mat, normal, o0a, color0, 0f, 0f, packedLight);
        addVertex(context.builder, mat, normal, o0b, color0, 0f, 1f, packedLight);
        addVertex(context.builder, mat, normal, o1b, color1, 1f, 1f, packedLight);
        addVertex(context.builder, mat, normal, o1a, color1, 1f, 0f, packedLight);
    }


    /**
     * 计算拖尾分段宽度（带尖锐化效果）
     */
    private float calculateSegmentWidth(float baseSize, float t, float sharpenStart) {
        float width = baseSize * (1.0f - t * 0.7f) * 0.5f;

        if (t >= sharpenStart) {
            float s = Mth.clamp((t - sharpenStart) / (1f - sharpenStart), 0f, 1f);
            width *= (1f - s);
        }

        return width;
    }

    /**
     * 添加单个顶点到渲染器
     */
    private void addVertex(VertexConsumer builder, Matrix4f mat, Matrix3f normal, Vec3 pos,
                          float[] color, float u, float v, int packedLight) {
        builder.vertex(mat, (float) pos.x, (float) pos.y, (float) pos.z)
               .color((int) (color[0] * 255), (int) (color[1] * 255), (int) (color[2] * 255), (int) (color[3] * 255))
               .uv(u, v)
               .overlayCoords(OverlayTexture.NO_OVERLAY)
               .uv2(packedLight)
               .normal(normal, 0f, 0f, 1f)
               .endVertex();
    }

    /**
     * 拖尾渲染上下文 - 封装渲染所需的共享数据
     */
    private static class TrailRenderContext {
        final VertexConsumer builder;
        final Vec3 camPos;
        final Vec3 entityPos;
        final TrailColor color;

        TrailRenderContext(VertexConsumer builder, Vec3 camPos, Vec3 entityPos, TrailColor color) {
            this.builder = builder;
            this.camPos = camPos;
            this.entityPos = entityPos;
            this.color = color;
        }
    }

    /**
     * 拖尾颜色计算辅助类
     */
    private static class TrailColor {
        private final float r, g, b;

        TrailColor(int hexColor) {
            this.r = ((hexColor >> 16) & 0xFF) / 255f;
            this.g = ((hexColor >> 8) & 0xFF) / 255f;
            this.b = (hexColor & 0xFF) / 255f;
        }

        /**
         * 计算颜色和透明度
         * @param t 位置比例 (0-1)
         * @param lerpFactor 颜色插值因子
         * @param alphaBase 基础透明度
         * @param alphaExponent 透明度衰减指数
         * @return [r, g, b, a]
         */
        float[] calculateColor(float t, float lerpFactor, float alphaBase, double alphaExponent) {
            float lerp = t * lerpFactor;
            float cr = r * (1f - lerp) + lerp;
            float cg = g * (1f - lerp) + lerp;
            float cb = b * (1f - lerp) + lerp;
            float alpha = (float) Math.pow(1f - t, alphaExponent) * alphaBase;

            return new float[]{cr, cg, cb, alpha};
        }
    }

}

