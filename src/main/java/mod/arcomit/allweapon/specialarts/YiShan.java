package mod.arcomit.allweapon.specialarts;

import mod.arcomit.allweapon.util.movement.EntityMovementUtils;
import mod.arcomit.allweapon.util.entity.FindEntityUtils;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 13:56
 * @Description: 一闪SA
 */
public class YiShan {
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
            target = FindEntityUtils.findTargetEntity(user, 10.0f , 60.0f, true);
        }

        if (target != null) {
            // 计算从玩家到目标的方向向量
//            Vec3 directionToTarget = target.position().subtract(user.position());
//            float distanceToTarget = target.distanceTo(user);
//            EntityMovementUtils.startLinearMove(
//                    user,
//                    directionToTarget,
//                    distanceToTarget,
//                    3
//            );
            Vec3 directionToTarget = user.position().subtract(target.position());
            float distanceToTarget = user.distanceTo(target);
            EntityMovementUtils.startLinearMove(
                    target,
                    directionToTarget,
                    distanceToTarget,
                    200
            );
        }else {
            // 没有目标时，朝玩家视线方向移动
            Vec3 lookDirection = user.getLookAngle();
            EntityMovementUtils.startLinearMove(
                    user,
                    lookDirection,
                    10.0,
                    3
            );
        }
    }
}
