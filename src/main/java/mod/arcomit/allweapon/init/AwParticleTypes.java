package mod.arcomit.allweapon.init;

import mod.arcomit.allweapon.AllWeapon;
import mod.arcomit.allweapon.client.particle.YiShanCracksParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-12-07 17:34
 * @Description: TODO
 */
@Mod.EventBusSubscriber(modid = AllWeapon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AwParticleTypes {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AllWeapon.MODID);

    public static final RegistryObject<SimpleParticleType> TEST_PARTICLE = PARTICLE_TYPES.register(
            "test_particle",
            () -> new SimpleParticleType(true)
    );

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(TEST_PARTICLE.get(), YiShanCracksParticle.Provider::new);
    }

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}