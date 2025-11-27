package mod.arcomit.allweapon.event;

import mod.arcomit.allweapon.init.AwBlades;
import mod.arcomit.allweapon.util.enchantment.EnchantmentUtils;
import mod.arcomit.allweapon.util.fluid.FluidDetectionUtils;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.recipe.RequestDefinition;
import mods.flammpfeil.slashblade.recipe.SlashBladeIngredient;
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 12:54
 * @Description: TODO：流刃若火
 */
@Mod.EventBusSubscriber
public class LiuRenRuoHuoHandler {
    private static final Map<UUID, Integer> LAVA_SOAK_TIME = new HashMap<>();

    @SubscribeEvent
    public static void bladeStandOnHit(SlashBladeEvent.BladeStandTickEvent event) {
        var bladeStand = event.getBladeStand();
        UUID bladeStandUUID = bladeStand.getUUID();
        if (!FluidDetectionUtils.isInLava(bladeStand)) {
            LAVA_SOAK_TIME.remove(bladeStandUUID);
            return;
        }

        var level = bladeStand.level();
        var slashBladeDefinitionRegistry = SlashBlade.getSlashBladeDefinitionRegistry(level);
        if (!slashBladeDefinitionRegistry.containsKey(AwBlades.LIURENRUOHUO)) {
            return;
        }

        var blade = event.getBlade();
        var in = SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                .refineCount(100)
                .addEnchantment(
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.FIRE_PROTECTION), 4)
                )
                .build()
        );
        if (!in.test(blade)){
            LAVA_SOAK_TIME.remove(bladeStandUUID);
            return;
        }

        ResourceKey<Level> dimension = level.dimension();
        if (dimension != Level.NETHER) {
            LAVA_SOAK_TIME.remove(bladeStandUUID);
            return;
        }

        int currentSoakTime = LAVA_SOAK_TIME.getOrDefault(bladeStandUUID, 0);
        currentSoakTime++;
        LAVA_SOAK_TIME.put(bladeStandUUID, currentSoakTime);

        if (currentSoakTime >= 600) {
            ItemStack newBlade = Objects.requireNonNull(slashBladeDefinitionRegistry.get(AwBlades.LIURENRUOHUO)).getBlade();
            var newBladeState = newBlade.getCapability(ItemSlashBlade.BLADESTATE).orElseThrow(NullPointerException::new);
            var oldBladeState = event.getSlashBladeState();
            newBladeState.setProudSoulCount(oldBladeState.getProudSoulCount());
            newBladeState.setKillCount(oldBladeState.getKillCount());
            newBladeState.setRefine(oldBladeState.getRefine());
            newBlade.getOrCreateTag().put("bladeState", newBladeState.serializeNBT());
            EnchantmentUtils.updateEnchantment(newBlade, blade);
            bladeStand.setItem(newBlade);

            LAVA_SOAK_TIME.remove(bladeStandUUID);
        }
    }
}
