package mod.arcomit.allweapon.util;

import mods.flammpfeil.slashblade.entity.IShootable;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.Comparator;
import java.util.List;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-24 13:04
 * @Description: 实体查找工具类，提供各种目标搜索功能
 */
public class FindEntityUtils {

    public static LivingEntity findTargetEntity(LivingEntity livingEntity, double maxAngle, double maxDist, boolean checkLineOfSight) {
        LivingEntity target = findClosestToCrosshair(livingEntity, maxAngle, maxDist);
        if (target != null) {
            return target;
        }
        return findNearestEntity(livingEntity, maxDist, checkLineOfSight);
    }

    public static LivingEntity findClosestToCrosshair(LivingEntity livingEntity, double maxAngle, double maxDist) {
        final Vec3 eyePos = livingEntity.getEyePosition(1.0f);
        final Vec3 lookVec = livingEntity.getLookAngle().normalize();
        final double cosThreshold = Math.cos(Math.toRadians(maxAngle));
        final double maxDistSqr = maxDist * maxDist;

        List<Entity> candidates = getInitialCandidates(livingEntity, eyePos, maxDist, maxDistSqr);

        return candidates.stream()
                .map(entity -> resolveTargetEntity(entity, livingEntity))
                .filter(java.util.Objects::nonNull)
                .distinct()
                .filter(entity -> isLivingEntityInAngle(entity, eyePos, lookVec, cosThreshold))
                .filter(entity -> TargetSelector.test.test(livingEntity, entity))
                .filter(entity -> hasLineOfSight(livingEntity, eyePos, entity))
                .min(Comparator.comparingDouble(entity ->
                        calculateCrosshairDistance(entity, eyePos, lookVec)))
                .orElse(null);
    }

    public static LivingEntity findNearestEntity(LivingEntity livingEntity, double radius, boolean checkLineOfSight) {
        if (livingEntity == null || !livingEntity.isAlive()) {
            return null;
        }

        AABB searchArea = createSearchArea(livingEntity, radius);
        Vec3 eyePos = checkLineOfSight ? livingEntity.getEyePosition(1.0f) : null;

        return livingEntity.level().getEntities(livingEntity, searchArea)
                .stream()
                .map(entity -> resolveTargetEntity(entity, livingEntity))
                .filter(java.util.Objects::nonNull)
                .distinct()
                .filter(entity -> TargetSelector.test.test(livingEntity, entity))
                .filter(entity -> !checkLineOfSight || hasLineOfSight(livingEntity, eyePos, entity))
                .min(Comparator.comparingDouble(livingEntity::distanceToSqr))
                .orElse(null);
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 解析目标实体，处理IShootable和PartEntity的情况
     * @param entity 原始实体
     * @param livingEntity 搜索者
     * @return 解析后的LivingEntity，如果不是有效目标则返回null
     */
    private static LivingEntity resolveTargetEntity(Entity entity, LivingEntity livingEntity) {
        // 处理PartEntity，获取顶层父实体
        Entity resolved = getTopParent(entity);

        // 如果是IShootable，尝试获取发射者
        if (resolved instanceof IShootable) {
            Entity shooter = ((IShootable) resolved).getShooter();
            if (shooter instanceof LivingEntity && shooter != livingEntity) {
                return (LivingEntity) shooter;
            }
        }

        // 返回LivingEntity或null
        return resolved instanceof LivingEntity ? (LivingEntity) resolved : null;
    }

    private static Entity getTopParent(Entity entity) {
        if (entity instanceof PartEntity<?>) {
            Entity parent = ((PartEntity<?>) entity).getParent();
            if (parent != null) {
                return getTopParent(parent);
            }
        }
        return entity;
    }

    private static List<Entity> getInitialCandidates(LivingEntity livingEntity, Vec3 eyePos,
                                                     double maxDist, double maxDistSqr) {
        return livingEntity.level().getEntitiesOfClass(
                Entity.class,
                new AABB(eyePos, eyePos).inflate(maxDist),
                entity -> entity.isAlive()
                        && entity.isPickable()
                        && eyePos.distanceToSqr(entity.position()) <= maxDistSqr
        );
    }

    private static boolean isLivingEntityInAngle(Entity entity, Vec3 eyePos,
                                                 Vec3 lookVec, double cosThreshold) {
        if (!(entity instanceof LivingEntity)) {
            return false;
        }

        Vec3 entityPos = entity.getBoundingBox().getCenter();
        Vec3 toEntity = entityPos.subtract(eyePos);
        double distSqr = toEntity.lengthSqr();

        if (distSqr < 0.0001) {
            return false;
        }

        double projection = toEntity.dot(lookVec);
        double actualCos = projection / Math.sqrt(distSqr);

        return actualCos >= cosThreshold;
    }


    private static boolean hasLineOfSight(LivingEntity livingEntity, Vec3 eyePos, Entity entity) {
        Vec3 entityPos = entity.getBoundingBox().getCenter();
        ClipContext context = new ClipContext(
                eyePos, entityPos,
                ClipContext.Block.VISUAL,
                ClipContext.Fluid.NONE,
                livingEntity
        );

        return livingEntity.level().clip(context).getType() != HitResult.Type.BLOCK;
    }

    private static double calculateCrosshairDistance(Entity entity, Vec3 eyePos, Vec3 lookVec) {
        Vec3 entityPos = entity.getBoundingBox().getCenter();
        Vec3 toEntity = entityPos.subtract(eyePos);
        double projection = lookVec.dot(toEntity);
        Vec3 closestPoint = eyePos.add(lookVec.scale(projection));

        return entityPos.distanceToSqr(closestPoint);
    }

    private static AABB createSearchArea(LivingEntity livingEntity, double radius) {
        return new AABB(
                livingEntity.getX() - radius, livingEntity.getY() - radius, livingEntity.getZ() - radius,
                livingEntity.getX() + radius, livingEntity.getY() + radius, livingEntity.getZ() + radius
        );
    }
}
