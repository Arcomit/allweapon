package mod.arcomit.allweapon.init;

import mod.arcomit.allweapon.AllWeapon;
import mod.arcomit.allweapon.specialarts.BaiGuHuanDie;
import mod.arcomit.allweapon.specialarts.HuanYingDie;
import mod.arcomit.allweapon.specialarts.YiShan;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.compat.playerAnim.PlayerAnimationOverrider;
import mods.flammpfeil.slashblade.compat.playerAnim.VmdAnimation;
import mods.flammpfeil.slashblade.event.client.UserPoseOverrider;
import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 13:43
 * @Description:  连招注册类
 */
@SuppressWarnings("removal")
public class AwComboStates {
    public static final DeferredRegister<ComboState> COMBO_STATES =
            DeferredRegister.create(ComboState.REGISTRY_KEY, AllWeapon.MODID);

    public static final RegistryObject<ComboState> SA_HUAN_YING_DIE = COMBO_STATES.register(
            "sa_huan_ying_die",
            ComboState.Builder.newInstance()
                    .startAndEnd(1100, 1122)
                    .priority(50)
                    .motionLoc(DefaultResources.ExMotionLocation)
                    .next(ComboState.TimeoutNext.buildFromFrame(5, entity -> SlashBlade.prefix("none")))
                    .nextOfTimeout(entity -> SlashBlade.prefix("aerial_rave_a1_end"))
                    .clickAction(HuanYingDie::doSpecialArts)
                    .addTickAction(UserPoseOverrider::resetRot)
                    .addHitEffect(StunManager::setStun)
                    ::build);

    public static final RegistryObject<ComboState> SA_BAI_GU_HUAN_DIE = COMBO_STATES.register(
            "sa_bai_gu_huan_die",
            ComboState.Builder.newInstance()
                    .startAndEnd(1100, 1122)
                    .priority(50)
                    .motionLoc(DefaultResources.ExMotionLocation)
                    .next(ComboState.TimeoutNext.buildFromFrame(5, entity -> SlashBlade.prefix("none")))
                    .nextOfTimeout(entity -> SlashBlade.prefix("aerial_rave_a1_end"))
                    .clickAction(BaiGuHuanDie::doSpecialArts)
                    .addTickAction(UserPoseOverrider::resetRot)
                    .addHitEffect(StunManager::setStun)
                    ::build);

    public static final RegistryObject<ComboState> SA_YI_SHAN = COMBO_STATES.register(
            "sa_yi_shan",
            ComboState.Builder.newInstance()
                    .startAndEnd(2000, 2019)
                    .priority(50)
                    .next(ComboState.TimeoutNext.buildFromFrame(5, entity -> SlashBlade.prefix("none")))
                    .nextOfTimeout(entity -> SlashBlade.prefix("rapid_slash_end2"))
                    .clickAction(YiShan::doSpecialArts)
                    .addTickAction(UserPoseOverrider::resetRot)
                    .addHitEffect(StunManager::setStun)
                    ::build);

    public static final RegistryObject<ComboState> SA_LIE_DI_MENG_CHONG= COMBO_STATES.register(
            "sa_lie_di_meng_chong",
            ComboState.Builder.newInstance()
                    .startAndEnd(2000, 2019)
                    .priority(50)
                    .next(ComboState.TimeoutNext.buildFromFrame(5, entity -> SlashBlade.prefix("none")))
                    .nextOfTimeout(entity -> SlashBlade.prefix("rapid_slash_end2"))
                    .clickAction(YiShan::doSpecialArts)
                    .addTickAction(UserPoseOverrider::resetRot)
                    .addHitEffect(StunManager::setStun)
                    ::build);

    public static final ResourceLocation MOTION_LOCATION = new ResourceLocation(SlashBlade.MODID,
            "model/pa/player_motion.vmd");
    public static void initAnimation() {
        PlayerAnimationOverrider.getInstance().getAnimation().put(
                SA_HUAN_YING_DIE.getId(),
                new VmdAnimation(MOTION_LOCATION, 1100, 1132, false).setBlendLegs(false));
        PlayerAnimationOverrider.getInstance().getAnimation().put(
                SA_BAI_GU_HUAN_DIE.getId(),
                new VmdAnimation(MOTION_LOCATION, 1100, 1132, false).setBlendLegs(false));
        PlayerAnimationOverrider.getInstance().getAnimation().put(
                SA_YI_SHAN.getId(),
                new VmdAnimation(MOTION_LOCATION, 2000, 2019, false).setBlendLegs(false));
        PlayerAnimationOverrider.getInstance().getAnimation().put(
                ComboStateRegistry.RAPID_SLASH_END2.getId(),
                new VmdAnimation(MOTION_LOCATION, 2054, 2073, false).setBlendLegs(false));
    }

    public static void register(IEventBus modEventBus) {
        COMBO_STATES.register(modEventBus);
        initAnimation();
    }
}
