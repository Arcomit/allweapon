package mod.arcomit.allweapon.util.movement;

import mod.arcomit.allweapon.network.PlayerMovementPacket;
import mod.arcomit.allweapon.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-24 14:59
 * @Description: 实体移动工具类
 */
@Mod.EventBusSubscriber
public class EntityMovementUtils {
    // 服务器端的移动信息
    private static final Map<LivingEntity, EntityMovementInfo> SERVER_MOVEMENT_INFO = new HashMap<>();
    // 客户端的移动信息
    private static final Map<LivingEntity, EntityMovementInfo> CLIENT_MOVEMENT_INFO = new HashMap<>();

    /**
     * 启动线性移动（仅服务器端调用）
     * 会自动同步到客户端
     */
    public static void startLinearMove(LivingEntity entity, Vec3 direction, double totalDistance, int totalTicks) {
        if (!entity.level().isClientSide) {
            if (SERVER_MOVEMENT_INFO.containsKey(entity)) {
                return;
            }
            SERVER_MOVEMENT_INFO.put(entity, new EntityMovementInfo(entity.getDeltaMovement(), direction, totalDistance, totalTicks));

            // 发送数据包到客户端
            if (entity instanceof ServerPlayer player) {
                PlayerMovementPacket packet = new PlayerMovementPacket(entity.getId(), direction, totalDistance, totalTicks);
                NetworkHandler.sendToPlayer(packet, player);
            }

        }
    }

    /**
     * 启动线性移动（客户端专用）
     * 由数据包调用，不要手动调用
     */
    public static void startLinearMoveClient(LivingEntity entity, Vec3 direction, double totalDistance, int totalTicks) {
        if (entity.level().isClientSide) {
            if (CLIENT_MOVEMENT_INFO.containsKey(entity)) {
                return;
            }
            CLIENT_MOVEMENT_INFO.put(entity, new EntityMovementInfo(entity.getDeltaMovement(), direction, totalDistance, totalTicks));
        }
    }

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        // 根据是否在客户端选择不同的Map
        Map<LivingEntity, EntityMovementInfo> movementMap = entity.level().isClientSide
            ? CLIENT_MOVEMENT_INFO
            : SERVER_MOVEMENT_INFO;

        if (movementMap.containsKey(entity)) {
            EntityMovementInfo movementInfo = movementMap.get(entity);
            int totalTicks = movementInfo.getTotalTicks();

            if (movementInfo.getMovedTick() < totalTicks) {
                double distancePerTick = movementInfo.getTotalDistance() / totalTicks;
                Vec3 normalizedDir = movementInfo.getDirection().normalize();
                Vec3 movement = normalizedDir.scale(distancePerTick);

                // 使用move方法进行移动，这样会正确处理碰撞和服务器验证
                entity.move(MoverType.SELF, movement);

                if (entity instanceof ServerPlayer player){
                    if (player.connection != null) {
                        player.connection.resetPosition();
                    }
                }

                // 重置delta movement以防止额外的移动
                entity.setDeltaMovement(Vec3.ZERO);

                movementInfo.setMovedTick(movementInfo.getMovedTick() + 1);
            } else {
                // 移动结束，恢复初始加速度
                entity.setDeltaMovement(movementInfo.getOldDeltaMovement());
                entity.hasImpulse = true;
                movementMap.remove(entity);
            }
        }
    }
}
