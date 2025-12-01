package mod.arcomit.allweapon;

import com.mojang.logging.LogUtils;
import mod.arcomit.allweapon.init.*;
import mod.arcomit.allweapon.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-08-02 16:48
 * @Description: MOD主类
 */
@SuppressWarnings("removal")
@Mod(AllWeapon.MODID)
public class AllWeapon {

    public static final String MODID = "allweapon";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AllWeapon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        AwItems.register(modEventBus);
        AwTabs.register(modEventBus);
        AwSpecialArts.register(modEventBus);
        AwComboStates.register(modEventBus);
        AwEntityTypes.register(modEventBus);
        AwParticleTypes.register(modEventBus);

        NetworkHandler.register();
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(AllWeapon.MODID, path);
    }
}
