package mod.arcomit.allweapon.client.event;

import mod.arcomit.allweapon.AllWeapon;
import mod.arcomit.allweapon.client.renderer.entity.HuanYingDieRenderer;
import mod.arcomit.allweapon.init.AwEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 17:50
 * @Description: 客户端事件处理类
 */
@Mod.EventBusSubscriber(modid = AllWeapon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AwEntityTypes.SUMMONED_SWORD.get(), HuanYingDieRenderer::new);
    }


}

