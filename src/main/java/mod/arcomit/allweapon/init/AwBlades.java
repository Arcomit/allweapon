package mod.arcomit.allweapon.init;

import mod.arcomit.allweapon.AllWeapon;
import mod.arcomit.allweapon.util.enchantment.EnchantmentUtils;
import mods.flammpfeil.slashblade.client.renderer.CarryType;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.PropertiesDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.RenderDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantments;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 13:43
 * @Description:  拔刀注册类
 */
public class AwBlades {
    public static final ResourceKey<SlashBladeDefinition> BAILAN = register("bailan");
    public static final ResourceKey<SlashBladeDefinition> BAIQIYUE = register("baiqiyue");
    public static final ResourceKey<SlashBladeDefinition> BAISHOUJIANWANG = register("baishoujianwang");
    public static final ResourceKey<SlashBladeDefinition> BAIYUEDING = register("baiyueding");
    public static final ResourceKey<SlashBladeDefinition> CHANSHIZHE = register("chanshizhe");
    public static final ResourceKey<SlashBladeDefinition> FENGSHEN = register("fengshen");
    public static final ResourceKey<SlashBladeDefinition> FENGZHIYING = register("fengzhiying");
    public static final ResourceKey<SlashBladeDefinition> GUANGJIAN = register("guangjian");
    public static final ResourceKey<SlashBladeDefinition> HUAMI = register("huami");
    public static final ResourceKey<SlashBladeDefinition> HUATIAN = register("huatian");
    public static final ResourceKey<SlashBladeDefinition> KUANGGU = register("kuanggu");
    public static final ResourceKey<SlashBladeDefinition> JIE = register("jie");
    public static final ResourceKey<SlashBladeDefinition> JIYI = register("jiyi");
    public static final ResourceKey<SlashBladeDefinition> LIURENRUOHUO = register("liurenruohuo");
    public static final ResourceKey<SlashBladeDefinition> LVLUO = register("lvluo");
    public static final ResourceKey<SlashBladeDefinition> QINGGUI = register("qinggui");
    public static final ResourceKey<SlashBladeDefinition> XIAOYUE = register("xiaoyue");
    public static final ResourceKey<SlashBladeDefinition> XUEZOU = register("xuezou");
    public static final ResourceKey<SlashBladeDefinition> YINGYUE = register("yingyue");
    public static final ResourceKey<SlashBladeDefinition> YOUMING = register("youming");
    public static final ResourceKey<SlashBladeDefinition> ZHANYUE = register("zhanyue");

    public static void registerAll(BootstapContext<SlashBladeDefinition> bootstrap){
        bootstrap.register(BAILAN,
                new SlashBladeDefinition(
                        BAILAN.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/bailan/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/bailan/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0F)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        Lists.newArrayList(),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(BAIQIYUE,
                new SlashBladeDefinition(
                        BAIQIYUE.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/baiqiyue/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/baiqiyue/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0F)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.ALL_DAMAGE_PROTECTION), 6)),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(BAISHOUJIANWANG,
                new SlashBladeDefinition(
                        BAISHOUJIANWANG.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/baishoujianwang/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/baishoujianwang/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0F)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.SHARPNESS), 10)),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(BAIYUEDING,
                new SlashBladeDefinition(
                        BAIYUEDING.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/baiyueding/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/baiyueding/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.ALL_DAMAGE_PROTECTION), 6)),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(CHANSHIZHE,
                new SlashBladeDefinition(
                        CHANSHIZHE.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/chanshizhe/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/chanshizhe/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.KNOCKBACK), 6),
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.PUNCH_ARROWS), 6),
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.INFINITY_ARROWS), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(FENGSHEN,
                new SlashBladeDefinition(
                        FENGSHEN.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/fengshen/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/fengshen/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.INFINITY_ARROWS), 20),
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.SHARPNESS), 20),
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.MOB_LOOTING), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(FENGZHIYING,
                new SlashBladeDefinition(
                        FENGZHIYING.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/fengzhiying/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/fengzhiying/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.FALL_PROTECTION), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(GUANGJIAN,
                new SlashBladeDefinition(
                        GUANGJIAN.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/guangjian/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/guangjian/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.INFINITY_ARROWS), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(HUAMI,
                new SlashBladeDefinition(
                        HUAMI.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/huami/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/huami/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.BLAST_PROTECTION), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(HUATIAN,
                new SlashBladeDefinition(
                        HUATIAN.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/huatiankuanggu/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/huatiankuanggu/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.BANE_OF_ARTHROPODS), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(KUANGGU,
                new SlashBladeDefinition(
                        KUANGGU.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/huatiankuanggu/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/huatiankuanggu/model_2.obj"))
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.SMITE), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(JIE,
                new SlashBladeDefinition(
                        JIE.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/jie/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/jie/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.SMITE), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(JIYI,
                new SlashBladeDefinition(
                        JIYI.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/jiyi/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/jiyi/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.UNBREAKING), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(LIURENRUOHUO,
                new SlashBladeDefinition(
                        LIURENRUOHUO.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/liurenruohuo/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/liurenruohuo/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.FIRE_PROTECTION), 20),
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.FIRE_ASPECT), 20),
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.FLAMING_ARROWS), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(LVLUO,
                new SlashBladeDefinition(
                        LVLUO.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/lvluo/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/lvluo/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        Lists.newArrayList(),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(QINGGUI,
                new SlashBladeDefinition(
                        QINGGUI.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/qinggui/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/qinggui/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        Lists.newArrayList(),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(XIAOYUE,
                new SlashBladeDefinition(
                        XIAOYUE.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/xiaoyue/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/xiaoyue/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        Lists.newArrayList(),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(XUEZOU,
                new SlashBladeDefinition(
                        XUEZOU.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/xuezou/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/xuezou/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.FROST_WALKER), 20),
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.RESPIRATION), 20),
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.AQUA_AFFINITY), 20),
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.FISHING_LUCK), 20),
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.DEPTH_STRIDER), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(YINGYUE,
                new SlashBladeDefinition(
                        YINGYUE.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/yingyue/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/yingyue/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        Lists.newArrayList(),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(YOUMING,
                new SlashBladeDefinition(
                        YOUMING.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/youming/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/youming/model.obj"))
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        List.of(
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.MOB_LOOTING), 20),
                                new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.SMITE), 20)
                        ),
                        AwTabs.ALL_WEAPON_TAB.getId()));

        bootstrap.register(ZHANYUE,
                new SlashBladeDefinition(
                        ZHANYUE.location(),
                        RenderDefinition.Builder.newInstance()
                                .textureName(AllWeapon.prefix("model/blade/zhanyue/texture.png"))
                                .modelName(AllWeapon.prefix("model/blade/zhanyue/model.obj"))
                                .standbyRenderType(CarryType.PSO2)
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .baseAttackModifier(4.0f)
                                .maxDamage(120)
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .build(),
                        Lists.newArrayList(),
                        AwTabs.ALL_WEAPON_TAB.getId()));

    }

    public static ResourceKey<SlashBladeDefinition> register(String id) {
        ResourceKey<SlashBladeDefinition> loc = ResourceKey.create(SlashBladeDefinition.REGISTRY_KEY,
                AllWeapon.prefix(id));
        return loc;
    }
}
