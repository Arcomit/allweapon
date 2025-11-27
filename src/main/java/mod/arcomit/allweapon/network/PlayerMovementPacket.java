package mod.arcomit.allweapon.network;

import mod.arcomit.allweapon.util.movement.EntityMovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 13:56
 * @Description: 玩家移动数据包，用于同步玩家的移动信息
 */
public class PlayerMovementPacket {
    private final int entityId;
    private final Vec3 direction;
    private final double totalDistance;
    private final int totalTicks;

    public PlayerMovementPacket(int entityId, Vec3 direction, double totalDistance, int totalTicks) {
        this.entityId = entityId;
        this.direction = direction;
        this.totalDistance = totalDistance;
        this.totalTicks = totalTicks;
    }

    /**
     * 编码数据包：将数据写入缓冲区
     */
    public static void encode(PlayerMovementPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeDouble(packet.direction.x);
        buf.writeDouble(packet.direction.y);
        buf.writeDouble(packet.direction.z);
        buf.writeDouble(packet.totalDistance);
        buf.writeInt(packet.totalTicks);
    }

    /**
     * 解码数据包：从缓冲区读取数据
     */
    public static PlayerMovementPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        Vec3 direction = new Vec3(x, y, z);
        double totalDistance = buf.readDouble();
        int totalTicks = buf.readInt();
        return new PlayerMovementPacket(entityId, direction, totalDistance, totalTicks);
    }

    /**
     * 处理数据包：在客户端执行
     */
    public static void handle(PlayerMovementPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // 确保在客户端线程执行
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(packet));
        });
        context.setPacketHandled(true);
    }

    /**
     * 客户端处理逻辑
     */
    private static void handleClient(PlayerMovementPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            Entity entity = minecraft.level.getEntity(packet.entityId);
            if (entity instanceof Player player) {
                // 在客户端启动移动
                EntityMovementUtils.startLinearMoveClient(
                        player,
                    packet.direction,
                    packet.totalDistance,
                    packet.totalTicks
                );
            }
        }
    }
}

