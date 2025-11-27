package mod.arcomit.allweapon.util.spawner;

import mod.arcomit.allweapon.entity.HuanYingDieEntity;
import mod.arcomit.allweapon.init.AwEntityTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * 幻影蝶实体使用示例
 *
 * 此类展示了如何创建和使用带有追踪功能的幻影蝶实体
 */
public class HuanYingDieSpawner {

    public static void shoot(Level level, LivingEntity shooter, float velocity, float inaccuracy, float damage, Entity trakingEntity, float trackingStrength, boolean enableAutoTrack, float autoTrackingRange) {
        shoot(
                level,
                shooter,
                shooter.getX(),
                shooter.getEyeY() - 0.1D,
                shooter.getZ(),
                shooter.getXRot(),
                shooter.getYRot(),
                velocity,
                inaccuracy,
                damage,
                trakingEntity,
                trackingStrength,
                enableAutoTrack,
                autoTrackingRange
                );
    }

    public static void shoot(Level level, LivingEntity shooter, double posX, double posY, double posZ, float pitch, float yaw, float velocity, float inaccuracy, float damage, Entity trakingEntity, float trackingStrength, boolean enableAutoTrack, float autoTrackingRange) {
        HuanYingDieEntity die = new HuanYingDieEntity(AwEntityTypes.SUMMONED_SWORD.get(), level);
        die.setOwner(shooter);
        die.setPos(posX, posY, posZ);
        die.setTrackingTarget(trakingEntity);
        die.setTrackingStrength(trackingStrength);
        die.setAutoTracking(enableAutoTrack);
        die.setAutoTrackingRange(autoTrackingRange);
        die.setBasicDamage(damage);
        die.shoot(shooter, pitch, yaw, velocity, inaccuracy);
        level.addFreshEntity(die);
    }
}

