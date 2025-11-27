package mod.arcomit.allweapon.network;

import mod.arcomit.allweapon.AllWeapon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 13:56
 * @Description: 网络处理类，负责注册和发送数据包
 */
public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        AllWeapon.prefix("main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    /**
     * 注册所有数据包
     */
    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(PlayerMovementPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
            .decoder(PlayerMovementPacket::decode)
            .encoder(PlayerMovementPacket::encode)
            .consumerMainThread(PlayerMovementPacket::handle)
            .add();
    }

    /**
     * 发送数据包给指定玩家
     */
    public static void sendToPlayer(Object packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    /**
     * 发送数据包给追踪该实体的所有玩家
     */
    public static void sendToTrackingEntity(Object packet, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), packet);
    }

    /**
     * 发送数据包给追踪该实体的所有玩家（包括实体自己，如果是玩家）
     */
    public static void sendToTrackingEntityAndSelf(Object packet, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), packet);
    }
}

