package mod.arcomit.allweapon.init;

import mod.arcomit.allweapon.AllWeapon;
import mods.flammpfeil.slashblade.SlashBladeCreativeGroup;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21
 * @Description: 创造物品栏注册类
 */
public class AwTabs {
    //创造物品栏注册表
    private static final DeferredRegister<CreativeModeTab> TBAS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AllWeapon.MODID);

    public static final RegistryObject<CreativeModeTab> ALL_WEAPON_TAB = TBAS.register("all_weapon_tab", () -> CreativeModeTab.builder()
            // 设置所要展示的页的名称
            .title(Component.translatable("item_group." + AllWeapon.MODID + ".tab"))
            // 设置页图标
            .icon(() -> {
                ItemStack blade = new ItemStack(SlashBladeItems.SLASHBLADE.get());
                blade.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(bladeState -> {
                    bladeState.setModel(AllWeapon.prefix("model/blade/fengshen/model.obj"));
                    bladeState.setTexture(AllWeapon.prefix("model/blade/fengshen/texture.png"));
                });
                return blade;
            })
            // 物品栏在拔刀剑创造物品栏之后
            .withTabsBefore(SlashBladeCreativeGroup.SLASHBLADE_GROUP.getKey())
            .build()
    );

    public static void register(IEventBus modEventBus) {
        TBAS.register(modEventBus);
    }
}
