package mod.arcomit.allweapon.event;

import mod.arcomit.allweapon.init.AwBlades;
import mod.arcomit.allweapon.util.enchantment.EnchantmentUtils;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.recipe.RequestDefinition;
import mods.flammpfeil.slashblade.recipe.SlashBladeIngredient;
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 12:54
 * @Description: 光剑获取方法
 */
@Mod.EventBusSubscriber
public class GuangJianHandler {

    @SubscribeEvent
    public static void bladeStandStruckByLightningEvent(EntityStruckByLightningEvent event) {
        if (!(event.getEntity() instanceof BladeStandEntity bladeStand)) {
            return;
        }
        var level = bladeStand.level();

        var slashBladeDefinitionRegistry = SlashBlade.getSlashBladeDefinitionRegistry(level);
        if (!slashBladeDefinitionRegistry.containsKey(AwBlades.GUANGJIAN)) {
            return;
        }

        var blade = bladeStand.getItem();
        var in = SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                .refineCount(100)
                .addEnchantment(
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.FALL_PROTECTION), 4)
                )
                .build()
        );
        if (!in.test(blade)){
            return;
        }

        BlockPos standPos = bladeStand.blockPosition();
        if (level.getBrightness(LightLayer.BLOCK, standPos) <= 10){
            return;
        }

        ItemStack newBlade = Objects.requireNonNull(slashBladeDefinitionRegistry.get(AwBlades.GUANGJIAN)).getBlade();
        var newBladeState = newBlade.getCapability(ItemSlashBlade.BLADESTATE).orElseThrow(NullPointerException::new);

        var oldBladeState = blade.getCapability(ItemSlashBlade.BLADESTATE).orElseThrow(NullPointerException::new);
        newBladeState.setProudSoulCount(oldBladeState.getProudSoulCount());
        newBladeState.setKillCount(oldBladeState.getKillCount());
        newBladeState.setRefine(oldBladeState.getRefine());
        newBlade.getOrCreateTag().put("bladeState", newBladeState.serializeNBT());
        EnchantmentUtils.updateEnchantment(newBlade, blade);
        bladeStand.setItem(newBlade);

        event.setCanceled(true);
    }
}
