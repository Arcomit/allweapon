package mod.arcomit.allweapon.init;

import mod.arcomit.allweapon.AllWeapon;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 13:41
 * @Description: SA注册类
 */
public class AwSpecialArts {
    public static final DeferredRegister<SlashArts> SPECIAL_ARTS =
            DeferredRegister.create(SlashArts.REGISTRY_KEY, AllWeapon.MODID);

    public static final RegistryObject<SlashArts> HUAN_YING_DIE = SPECIAL_ARTS.register("huan_ying_die",
            () -> new SlashArts((e) ->AwComboStates.SA_HUAN_YING_DIE.getId())
                    .setComboStateJust((e) ->AwComboStates.SA_HUAN_YING_DIE.getId())
    );

    public static final RegistryObject<SlashArts> BAI_GU_HUAN_DIE = SPECIAL_ARTS.register("bai_gu_huan_die",
            () -> new SlashArts((e) ->AwComboStates.SA_BAI_GU_HUAN_DIE.getId())
                    .setComboStateJust((e) ->AwComboStates.SA_BAI_GU_HUAN_DIE.getId())
    );

    public static final RegistryObject<SlashArts> YI_SHAN = SPECIAL_ARTS.register("yi_shan",
            () -> new SlashArts((e) ->AwComboStates.SA_YI_SHAN.getId())
                    .setComboStateJust((e) ->AwComboStates.SA_YI_SHAN.getId())
    );

    public static final RegistryObject<SlashArts> LIE_DI_MENG_CHONG = SPECIAL_ARTS.register("lie_di_meng_chong",
            () -> new SlashArts((e) ->AwComboStates.SA_LIE_DI_MENG_CHONG.getId())
                    .setComboStateJust((e) ->AwComboStates.SA_LIE_DI_MENG_CHONG.getId())
    );

    public static void register(IEventBus modEventBus) {
        SPECIAL_ARTS.register(modEventBus);
    }
}
