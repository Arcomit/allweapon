package mod.arcomit.allweapon.init;

import mod.arcomit.allweapon.AllWeapon;
import mod.arcomit.allweapon.entity.HuanYingDieEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 17:46
 * @Description: 实体类型注册类
 */
public class AwEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AllWeapon.MODID);

    public static final RegistryObject<EntityType<HuanYingDieEntity>> SUMMONED_SWORD =
            ENTITY_TYPES.register("summoned_sword", () -> EntityType.Builder
                    .of(HuanYingDieEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("summoned_sword"));

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
