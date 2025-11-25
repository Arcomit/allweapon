package mod.arcomit.allweapon.specialarts;

import mod.arcomit.allweapon.util.FindEntityUtils;
import mod.arcomit.allweapon.util.entity.HuanYingDieSpawner;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 13:53
 * @Description: 幻影蝶SA
 */
public class HuanYingDie {
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

        for (int i = 0; i < 8; i++) {
            if (target != null) {
                // 有目标：在目标15格外的随机位置生成，指向并追踪目标
                Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);

                // 生成随机方向的单位向量
                double theta = user.getRandom().nextDouble() * 2 * Math.PI; // 水平角度
                double phi = user.getRandom().nextDouble() * Math.PI; // 垂直角度

                double x = Math.sin(phi) * Math.cos(theta);
                double y = Math.cos(phi);
                double z = Math.sin(phi) * Math.sin(theta);

                Vec3 randomDir = new Vec3(x, y, z).normalize();
                Vec3 spawnPos = targetPos.add(randomDir.scale(15));

                // 计算从生成位置指向目标的方向
                Vec3 direction = targetPos.subtract(spawnPos).normalize();
                float yaw = (float) Math.toDegrees(Math.atan2(-direction.x, direction.z));
                float pitch = (float) Math.toDegrees(Math.asin(-direction.y));

                HuanYingDieSpawner.shoot(
                        user.level(),
                        user,
                        spawnPos.x,
                        spawnPos.y,
                        spawnPos.z,
                        pitch,
                        yaw,
                        1.5f,
                        0.0f,
                        10.0f,
                        target,
                        0.1f,
                        true,
                        12.0f
                );
            } else {
                // 没有目标：在玩家周围生成，朝玩家视线方向发射
                double angle = (i / 8.0) * 2 * Math.PI;
                double radius = 2.0;

                double offsetX = Math.cos(angle) * radius;
                double offsetZ = Math.sin(angle) * radius;

                Vec3 userPos = user.position();
                double spawnX = userPos.x + offsetX;
                double spawnY = user.getEyeY();
                double spawnZ = userPos.z + offsetZ;

                HuanYingDieSpawner.shoot(
                        user.level(),
                        user,
                        spawnX,
                        spawnY,
                        spawnZ,
                        user.getXRot(),
                        user.getYRot(),
                        1.5f,
                        5.0f,
                        10.0f,
                        null,
                        0.1f,
                        true,
                        12.0f
                );
            }
        }
    }
}
