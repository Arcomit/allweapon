package mod.arcomit.allweapon.specialarts;

import mod.arcomit.allweapon.util.FindEntityUtils;
import mod.arcomit.allweapon.util.entity.HuanYingDieSpawner;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 13:56
 * @Description: 百谷幻蝶SA
 */
public class BaiGuHuanDie {
    public static void doSpecialArts(LivingEntity user) {
        if (user.level().isClientSide()) {
            return;
        }

        var stack = user.getMainHandItem();
        if (!(stack.getItem() instanceof ItemSlashBlade)) {
            return;
        }

        var bladeState = stack.getCapability(ItemSlashBlade.BLADESTATE).orElseThrow(NullPointerException::new);
        Entity targetEntity = bladeState.getTargetEntity(user.level());
        LivingEntity target;
        if (targetEntity != null && targetEntity instanceof LivingEntity) {
            target = (LivingEntity) targetEntity;
        } else {
            target = FindEntityUtils.findClosestToCrosshair(user, 10.0f , 30.0f);
        }

        for (int i = 0; i < 40; i++) {
            Vec3 centerPos;

            if (target != null) {
                // 有目标：使用目标位置作为球心
                centerPos = target.position().add(0, target.getBbHeight() / 2, 0);
            } else {
                // 没有目标：使用发射者位置作为球心
                centerPos = user.position().add(0, user.getBbHeight() / 2, 0);
            }

            // 在半径为8的球内生成随机位置
            double theta = user.getRandom().nextDouble() * 2 * Math.PI; // 水平角度
            double phi = user.getRandom().nextDouble() * Math.PI; // 垂直角度
            double radius = user.getRandom().nextDouble() * 8.0; // 随机半径 0-8

            double x = Math.sin(phi) * Math.cos(theta);
            double y = Math.cos(phi);
            double z = Math.sin(phi) * Math.sin(theta);

            Vec3 randomDir = new Vec3(x, y, z).normalize();
            Vec3 spawnPos = centerPos.add(randomDir.scale(radius));

            // 生成随机发射方向
            double shootTheta = user.getRandom().nextDouble() * 2 * Math.PI;
            double shootPhi = user.getRandom().nextDouble() * Math.PI;

            double shootX = Math.sin(shootPhi) * Math.cos(shootTheta);
            double shootY = Math.cos(shootPhi);
            double shootZ = Math.sin(shootPhi) * Math.sin(shootTheta);

            Vec3 shootDir = new Vec3(shootX, shootY, shootZ).normalize();
            float yaw = (float) Math.toDegrees(Math.atan2(-shootDir.x, shootDir.z));
            float pitch = (float) Math.toDegrees(Math.asin(-shootDir.y));

            // 随机速度 (0.8 到 2.5)
            float randomSpeed = 0.8f + user.getRandom().nextFloat() * 1.7f;

            float randomTracking = user.getRandom().nextFloat() / 2;

            HuanYingDieSpawner.shoot(
                    user.level(),
                    user,
                    spawnPos.x,
                    spawnPos.y,
                    spawnPos.z,
                    pitch,
                    yaw,
                    randomSpeed,
                    0.0f,
                    0.1f,
                    target,
                    randomTracking,
                    true,
                    12.0f
            );
        }
    }
}
