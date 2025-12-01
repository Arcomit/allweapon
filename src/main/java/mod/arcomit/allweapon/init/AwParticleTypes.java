package mod.arcomit.allweapon.init;

import mod.arcomit.allweapon.AllWeapon;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-28 12:43
 * @Description: TODO
 */
public class AwParticleTypes {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AllWeapon.MODID);

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
