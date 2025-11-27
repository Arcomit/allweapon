package mod.arcomit.allweapon.util.movement;

import lombok.*;
import net.minecraft.world.phys.Vec3;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-24 15:02
 * @Description: TODO
 */
@Getter
@RequiredArgsConstructor()
public class EntityMovementInfo {
    // 移动前的加速度
    private final Vec3 oldDeltaMovement;
    // 移动的方向
    private final Vec3 direction;
    // 移动的总距离
    private final double totalDistance;
    // 移动的总刻数
    private final int totalTicks;
    // 当前已经移动过的刻数
    @Setter
    private int movedTick;

}
